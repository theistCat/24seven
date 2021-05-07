package uz.usoft.a24seven.ui.category.selectedSubCategory

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentSelectedSubCategoryBinding
import uz.usoft.a24seven.databinding.SortBottomsheetBinding
import uz.usoft.a24seven.network.models.CartItem
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Variables
import uz.usoft.a24seven.ui.filter.FilterFragment
import uz.usoft.a24seven.ui.products.ProductViewModel
import uz.usoft.a24seven.utils.*


class SelectedSubCategoryFragment : BaseFragment<FragmentSelectedSubCategoryBinding>(FragmentSelectedSubCategoryBinding::inflate),
    DrawerLayout.DrawerListener {

    private lateinit var adapter: ProductPagingListAdapter
    private val safeArgs: SelectedSubCategoryFragmentArgs by navArgs()
    private val productViewModel: ProductViewModel by viewModel()

    private lateinit var sortBottomSheet: BottomSheetDialog
    private var _bottomSheetBinding:SortBottomsheetBinding?=null
    private val bottomSheetBinding get() = _bottomSheetBinding!!

    private var orderBy=Variables.sortBy[1]?:""

    private var updatePosition:Int=-1
    private var updateValue:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
        getProducts()
    }

    private fun getProducts() {
        lifecycleScope.launch {
            productViewModel.getProductsResponse(safeArgs.subCategoryId,orderBy.trim()).collect {
                adapter.submitData(it)
                return@collect
            }
        }
    }


    private fun applyFilter() {
        val filterFragment=(requireActivity().supportFragmentManager.findFragmentByTag("filterFragmentSideSheet") as FilterFragment)
        val filter=filterFragment.filter
        val filterOptions=HashMap<String,String>()
        filter.forEach{
            if(filter[it.key]?.isNotEmpty()==true)
            {
                it.value.forEach {attribute->
                    val keyId="filter[${filterOptions.size}][id]"
                    val keyAttribute="filter[${filterOptions.size}][attribute]"
                    filterOptions[keyId]=it.key
                    filterOptions[keyAttribute]=attribute
                }
            }
        }

        Log.d("filterT","$filterOptions")

        if(filterOptions.isNotEmpty())
            lifecycleScope.launch {
                productViewModel.getFilteredProductsResponse(safeArgs.subCategoryId,orderBy,filterOptions = filterOptions).collect {
                    adapter.submitData(it)
                    return@collect
                }
            }
        else if(filterFragment.resetFilter) {
            getProducts()
            filterFragment.resetFilter=false
        }
    }


    override fun setUpRecyclers() {
        binding.selectedSubCategoryRecycler.adapter = adapter
        binding.selectedSubCategoryRecycler.layoutManager = object:GridLayoutManager(requireContext(), 2){
            override fun onLayoutCompleted(state: RecyclerView.State?) {
                super.onLayoutCompleted(state)
                Log.d("Recycler","change decoreator")
//                if(binding.selectedSubCategoryRecycler.itemDecorationCount>0) {
//                    binding.selectedSubCategoryRecycler.removeItemDecorationAt(0)
//                    Log.d("Recycler","change decoreator")
//                    binding.selectedSubCategoryRecycler.addItemDecoration(
//                        SpacesItemDecoration(
//                            toDp(
//                                16
//                            )
//                        )
//                    )
//                }
            }

            override fun onItemsMoved(
                recyclerView: RecyclerView,
                from: Int,
                to: Int,
                itemCount: Int
            ) {
                super.onItemsMoved(recyclerView, from, to, itemCount)

                Log.d("Recycler","items moved change decorator")
            }
        }
        binding.selectedSubCategoryRecycler.addItemDecoration(SpacesItemDecoration(toDp(16)))
    }

    override fun setUpOnClickListeners() {
        binding.filter.setOnClickListener {
            mainActivity.openDrawer()
        }
        binding.sortBy.setOnClickListener {
            sortBottomSheet.show()
        }

        adapter.onFavClick = {product,position->
            updatePosition = position
            updateValue = !product.is_favorite

            if(!product.is_favorite) {
                productViewModel.addFav(product.id)
            }
            else{
                productViewModel.removeFav(product.id)
            }
        }

        adapter.addToCart={
            productViewModel.addToCart(CartItem(it.id,1))
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

    fun onSort(option: View)
    {
        when(option.id)
        {
            bottomSheetBinding.sortByNew.id->{
                binding.sortBy.text=getString(R.string.sort_by_new)
                orderBy=Variables.sortBy[0]!!
            }
            bottomSheetBinding.sortByPopular.id->{
                binding.sortBy.text=getString(R.string.sort_by_popular)
                orderBy=Variables.sortBy[1]!!
            }
            bottomSheetBinding.sortByCheap.id->{
                binding.sortBy.text=getString(R.string.sort_by_cheap)
                orderBy=Variables.sortBy[2]!!
            }
            bottomSheetBinding.sortByExpensive.id->{
                binding.sortBy.text=getString(R.string.sort_by_expensive)
                orderBy=Variables.sortBy[3]!!
            }
        }
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
            val subCategoryName=safeArgs.subCategoryName
            val productId=it.id
            val action =
                SelectedSubCategoryFragmentDirections.actionNavSelectedSubCategoryToNavSelectedProduct(subCategoryName, productId)
            navigate(action)
        }
    }

    override fun setUpUI() {
        mainActivity.setTitle(safeArgs.subCategoryName)
        mainActivity.drawerLayout.addDrawerListener(this)
        _bottomSheetBinding= SortBottomsheetBinding.inflate(layoutInflater)
        sortBottomSheet = createBottomSheet(bottomSheetBinding.root)

    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)

        adapter.update(updatePosition,updateValue)
    }
    override fun setUpObservers() {

        observeEvent(productViewModel.favResponse,::handle)

        productViewModel.characteristics.observe(
            viewLifecycleOwner, Observer { characteristics ->
                characteristics?.let {
                    (requireActivity().supportFragmentManager.findFragmentByTag("filterFragmentSideSheet") as FilterFragment).characteristics.value=it
                }
            }
        )

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                //progressBar.isVisible = loadStates.refresh is LoadState.Loading
                //retry.isVisible = loadState.refresh !is LoadState.Loading
                when(loadStates.refresh)
                {
                    is LoadState.Error->{
                        val error = loadStates.refresh as LoadState.Error
                        if (error.error is NoConnectivityException)
                        {
                            showNoConnectionDialog(this@SelectedSubCategoryFragment::onRetry)
                        }
                    }
                    is LoadState.Loading->{
                        hideNoConnectionDialog()
                        showLoadingDialog()
                    }
                    else->{
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