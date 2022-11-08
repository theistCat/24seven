package a24seven.uz.ui.category.selectedSubCategory

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import a24seven.uz.MainActivity
import a24seven.uz.R
import a24seven.uz.data.PrefManager
import a24seven.uz.databinding.FragmentSelectedSubCategoryBinding
import a24seven.uz.databinding.SortBottomsheetBinding
import a24seven.uz.network.models.CartItem
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.network.utils.Resource
import a24seven.uz.ui.filter.FilterFragment
import a24seven.uz.ui.products.ProductViewModel
import a24seven.uz.utils.SpacesItemDecoration
import a24seven.uz.utils.navigate
import a24seven.uz.utils.observeEvent
import a24seven.uz.utils.showSnackbar
import a24seven.uz.utils.toDp
import a24seven.uz.utils.*


class SelectedSubCategoryFragment :
    BaseFragment<FragmentSelectedSubCategoryBinding>(FragmentSelectedSubCategoryBinding::inflate),
    DrawerLayout.DrawerListener {

    private lateinit var adapter: ProductPagingListAdapter
    private val safeArgs: SelectedSubCategoryFragmentArgs by navArgs()
    private val productViewModel: ProductViewModel by viewModel()

    private lateinit var sortBottomSheet: BottomSheetDialog
    private var _bottomSheetBinding: SortBottomsheetBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!

    private var orderBy = mapOf("sort[id]" to "desc")

    val changeDecorator = MutableLiveData<Boolean>()
    var itemsMoved: Boolean = false

    private var updatePosition: Int = -1
    private var updateValue: Boolean = false

    private var cartItemPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
        getProducts()

        changeDecorator.value = false
    }

    private fun getProducts() {
        lifecycleScope.launch {
            productViewModel.getProductsResponse(safeArgs.subCategoryId, orderBy).collect {
                adapter.submitData(it)
                return@collect
            }
        }
    }


    private fun applyFilter() {
        val filterFragment =
            (requireActivity().supportFragmentManager.findFragmentByTag("filterFragmentSideSheet") as FilterFragment)
        val filter = filterFragment.filter
        val filterOptions = HashMap<String, String>()
        filter.forEach {
            if (filter[it.key]?.isNotEmpty() == true) {
                it.value.forEach { attribute ->
                    val keyId = "filter[${filterOptions.size}][id]"
                    val keyAttribute = "filter[${filterOptions.size}][attribute]"
                    filterOptions[keyId] = it.key
                    filterOptions[keyAttribute] = attribute
                }
            }
        }

        Log.d("filterT", "$filterOptions")

        if (filterOptions.isNotEmpty())
            lifecycleScope.launch {
                productViewModel.getFilteredProductsResponse(
                    safeArgs.subCategoryId,
                    orderBy,
                    filterOptions = filterOptions
                ).collect {
                    adapter.submitData(it)
                    return@collect
                }
            }
        else if (filterFragment.resetFilter) {
            getProducts()
            filterFragment.resetFilter = false
        }
    }


    override fun setUpRecyclers() {

        binding.selectedSubCategoryRecycler.adapter = adapter
        binding.selectedSubCategoryRecycler.addItemDecoration(SpacesItemDecoration(toDp(16)))

        binding.selectedSubCategoryRecycler.layoutManager =
            object : GridLayoutManager(requireContext(), 2) {
                override fun onLayoutCompleted(state: RecyclerView.State?) {
                    super.onLayoutCompleted(state)
                    changeDecorator.value = itemsMoved

                }


                override fun onItemsMoved(
                    recyclerView: RecyclerView,
                    from: Int,
                    to: Int,
                    itemCount: Int
                ) {
                    super.onItemsMoved(recyclerView, from, to, itemCount)
                    itemsMoved = true

                }
            }
    }

    override fun setUpOnClickListeners() {
        binding.filter.setOnClickListener {
            mainActivity.openDrawer()
        }
        binding.sortBy.setOnClickListener {
            sortBottomSheet.show()
        }

        adapter.onFavClick = { product, position ->
            updatePosition = position
            updateValue = !product.is_favorite

            if (!product.is_favorite) {
                productViewModel.addFav(product.id)
            } else {
                productViewModel.removeFav(product.id)
            }
        }

        adapter.addToCart = {
            productViewModel.storeCart(CartItem(it.id, 1))

            productViewModel.storeCartResponse.observe(
                viewLifecycleOwner, Observer { result ->

                    result.getContentIfNotHandled()?.let { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                onLoading()
                            }
                            is Resource.Success -> {
                                //viewModel.st(productsList)
                                hideLoadingDialog()
                                it.is_cart = true
                                PrefManager.getInstance(requireContext()).edit()
                                    .putBoolean(it.id.toString(), true).apply()
                                adapter.notifyDataSetChanged()
                            }
                            is Resource.GenericError -> {
                                onGenericError(resource)
                            }
                            is Resource.Error -> {
                                onError(resource)
                            }
                        }
                    }
                    result?.let {

                    }
                }
            )
        }


        //bottomsheet

        bottomSheetBinding.sortByNew.setOnClickListener {
            onSort(it)
        }
        bottomSheetBinding.sortByPopular.setOnClickListener {
            onSort(it)
        }
        bottomSheetBinding.sortByCheap.setOnClickListener {
            onSort(it)
        }
        bottomSheetBinding.sortByExpensive.setOnClickListener {
            onSort(it)
        }
    }

    fun onSort(option: View) {
        val queryMap = mutableMapOf<String, String>()
        when (option.id) {
            bottomSheetBinding.sortByNew.id -> {
                binding.sortBy.text = getString(R.string.sort_by_new)
                queryMap["sort[id]"] = "desc"
            }
            bottomSheetBinding.sortByPopular.id -> {
                queryMap["sort[views]"] = "desc"
                binding.sortBy.text = getString(R.string.sort_by_popular)
            }
            bottomSheetBinding.sortByCheap.id -> {
                queryMap["sort[price]"] = "asc"
                binding.sortBy.text = getString(R.string.sort_by_cheap)
            }
            bottomSheetBinding.sortByExpensive.id -> {
                queryMap["sort[price]"] = "desc"
                binding.sortBy.text = getString(R.string.sort_by_expensive)
            }
        }
        orderBy = queryMap

        sortBottomSheet.dismiss()
        getProducts()
    }

    override fun onRetry() {
        getProducts()
        mainActivity.showToolbar()
        mainActivity.showBottomNavigation()
    }

    private fun setUpAdapter() {
        adapter = ProductPagingListAdapter(requireContext())
        adapter.onItemClick = {
            val subCategoryName = safeArgs.subCategoryName
            val productId = it.id
            val action =
                SelectedSubCategoryFragmentDirections.actionNavSelectedSubCategoryToNavSelectedProduct(
                    selectedCategoryName = subCategoryName,
                    productId = productId
                )
            navigate(action)
        }
    }

    override fun setUpUI() {
        mainActivity.setTitle(safeArgs.subCategoryName)
        mainActivity.drawerLayout.addDrawerListener(this)
        _bottomSheetBinding = SortBottomsheetBinding.inflate(layoutInflater)
        sortBottomSheet =
            createBottomSheet(bottomSheetBinding.root)

    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)

        adapter.update(updatePosition, updateValue)
    }

    override fun setUpObservers() {

        observeEvent(productViewModel.favResponse, ::handle)

        changeDecorator.observe(
            viewLifecycleOwner, Observer { result ->
                result?.let {
                    if (it) {
                        Handler().postDelayed({
                            if (binding.selectedSubCategoryRecycler.itemDecorationCount > 0) {
                                binding.selectedSubCategoryRecycler.removeItemDecorationAt(0)
                                binding.selectedSubCategoryRecycler.addItemDecoration(
                                    SpacesItemDecoration(
                                        toDp(
                                            16
                                        )
                                    )
                                )
                                itemsMoved = false
                            }
                        }, 200)

                    }
                }
            }
        )

        productViewModel.addToCartResponse.observe(
            viewLifecycleOwner, Observer { result ->
                result?.let {
                    if (it.toInt() != -1) {

                        PrefManager.getInstance(requireContext()).edit()
                            .putBoolean(it.toString(), true).apply()
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        )

        productViewModel.characteristics.observe(
            viewLifecycleOwner, Observer { characteristics ->
                characteristics?.let {
                    (requireActivity().supportFragmentManager.findFragmentByTag("filterFragmentSideSheet") as FilterFragment).characteristics.value =
                        it
                }
            }
        )

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                //progressBar.isVisible = loadStates.refresh is LoadState.Loading
                //retry.isVisible = loadState.refresh !is LoadState.Loading
                when (loadStates.refresh) {
                    is LoadState.Error -> {
                        val error = loadStates.refresh as LoadState.Error

                        hideLoadingDialog()
                        if (error.error is NoConnectivityException) {
                            showNoConnectionDialog(this@SelectedSubCategoryFragment::onRetry)
                        } else {
                            showSnackbar(error.error.message.toString())
                        }
                    }
                    is LoadState.Loading -> {
                        hideNoConnectionDialog()
                        showLoadingDialog()
                    }
                    else -> {
                        hideNoConnectionDialog()
                        hideLoadingDialog()
                    }
                }

            }
        }
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        //not needed
    }

    override fun onDrawerOpened(drawerView: View) {
        //not needed
    }

    override fun onDrawerClosed(drawerView: View) {
        applyFilter()
    }


    override fun onDrawerStateChanged(newState: Int) {
        //not needed
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).drawerLayout.removeDrawerListener(this)
    }


}