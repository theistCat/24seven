package uz.usoft.a24seven.ui.category.selectedSubCategory

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentSelectedSubCategoryBinding
import uz.usoft.a24seven.databinding.SortBottomsheetBinding
import uz.usoft.a24seven.network.models.CartItem
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Variables
import uz.usoft.a24seven.ui.products.ProductViewModel
import uz.usoft.a24seven.utils.*


class SelectedSubCategoryFragment : BaseFragment<FragmentSelectedSubCategoryBinding>(FragmentSelectedSubCategoryBinding::inflate) {

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
            productViewModel.getProductsResponse(safeArgs.subCategoryId,orderBy).collect {
                adapter.submitData(it)
                return@collect
            }
        }
    }


    override fun setUpRecyclers() {
        binding.selectedSubCategoryRecycler.adapter = adapter
        binding.selectedSubCategoryRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
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
                orderBy=Variables.sortBy[2]!!.trim()
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
        _bottomSheetBinding= SortBottomsheetBinding.inflate(layoutInflater)
        sortBottomSheet = createBottomSheet(bottomSheetBinding.root)
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)

        adapter.update(updatePosition,updateValue)
    }
    override fun setUpObservers() {

        observeEvent(productViewModel.favResponse,::handle)

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
}