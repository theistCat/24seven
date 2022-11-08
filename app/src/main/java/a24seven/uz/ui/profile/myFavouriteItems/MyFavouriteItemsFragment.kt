package a24seven.uz.ui.profile.myFavouriteItems

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_my_favourite_items.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import a24seven.uz.MainActivity
import a24seven.uz.R
import a24seven.uz.data.PrefManager
import a24seven.uz.databinding.FragmentMyFavouriteItemsBinding
import a24seven.uz.databinding.SortBottomsheetBinding
import a24seven.uz.network.models.CartItem
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.ui.category.selectedSubCategory.ProductPagingListAdapter
import a24seven.uz.ui.profile.ProfileViewModel
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.SpacesItemDecoration
import a24seven.uz.utils.navigate
import a24seven.uz.utils.observeEvent
import a24seven.uz.utils.showSnackbar
import a24seven.uz.utils.toDp
import a24seven.uz.utils.*

class MyFavouriteItemsFragment :
    BaseFragment<FragmentMyFavouriteItemsBinding>(FragmentMyFavouriteItemsBinding::inflate) {


    private val viewModel: ProfileViewModel by viewModel()

    private lateinit var adapter: ProductPagingListAdapter
    private lateinit var sortBottomSheet: BottomSheetDialog

    private var orderBy: Map<String, String> = mapOf("sort[id]" to "desc")
    private var _bottomSheetBinding: SortBottomsheetBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!

    private var updatePosition: Int = -1
    private var updateValue: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapters()
        getFavProducts()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.refresh.setOnRefreshListener {
            getFavProducts()
        }
    }

    override fun setUpUI() {
        super.setUpUI()

        _bottomSheetBinding = SortBottomsheetBinding.inflate(layoutInflater)
        sortBottomSheet =
            createBottomSheet(bottomSheetBinding.root)
    }

    private fun getFavProducts() {
        lifecycleScope.launch {
            viewModel.getFavProductsResponse(orderBy).collect {
                adapter.submitData(it)
                return@collect
            }
        }
    }

    override fun onRetry() {
        super.onRetry()
        getFavProducts()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setUpObservers() {
        observeEvent(viewModel.favResponse, ::handle)

        viewModel.addToCartResponse.observe(
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

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                //progressBar.isVisible = loadStates.refresh is LoadState.Loading
                //retry.isVisible = loadState.refresh !is LoadState.Loading
                when (loadStates.refresh) {
                    is LoadState.Error -> {
                        hideLoadingDialog()
                        val error = loadStates.refresh as LoadState.Error
                        if (error.error is NoConnectivityException) {
                            showNoConnectionDialog(this@MyFavouriteItemsFragment::onRetry)
                        } else {
                            showSnackbar(error.error.message.toString())
                            binding.refresh.isRefreshing = false
                        }
                    }
                    is LoadState.Loading -> {
                        hideNoConnectionDialog()
                        showLoadingDialog()
                    }
                    else -> {
                        hideNoConnectionDialog()
                        hideLoadingDialog()
                        binding.refresh.isRefreshing = false
                    }
                }

            }
        }
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        adapter.update(updatePosition, updateValue)
    }


    private fun setUpAdapters() {
        adapter = ProductPagingListAdapter(requireContext())
        adapter.onItemClick = {
            val action =
                MyFavouriteItemsFragmentDirections.actionNavMyFavouriteItemsToNavSelectedProduct(
                    selectedCategoryName = resources.getString(R.string.title_myFavourites), productId = it.id
                )
            navigate(action)
        }
        adapter.onFavClick = { product, position ->
            updatePosition = position
            updateValue = !product.is_favorite

            if (!product.is_favorite) {
                viewModel.addFav(product.id)
            } else {
                viewModel.removeFav(product.id)
            }
        }

        adapter.addToCart = { product ->
            viewModel.addToCart(CartItem(product.id, 1))
        }

    }

    override fun setUpRecyclers() {
        binding.favouriteProductsRecycler.adapter = adapter
        binding.favouriteProductsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.favouriteProductsRecycler.addItemDecoration(SpacesItemDecoration(toDp(16)))
    }

    override fun setUpOnClickListeners() {

        binding.filter.setOnClickListener {
            (requireActivity() as MainActivity).openDrawer()
        }


        binding.sortBy.setOnClickListener {
            sortBottomSheet.show()
        }

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
        val queryMap = mutableMapOf<String,String>()
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
        getFavProducts()
    }


}