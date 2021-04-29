package uz.usoft.a24seven.ui.profile.myFavouriteItems

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentMyFavouriteItemsBinding
import uz.usoft.a24seven.databinding.SortBottomsheetBinding
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Variables
import uz.usoft.a24seven.ui.category.selectedSubCategory.ProductPagingListAdapter
import uz.usoft.a24seven.ui.profile.ProfileViewModel
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.*

class MyFavouriteItemsFragment : BaseFragment<FragmentMyFavouriteItemsBinding>(FragmentMyFavouriteItemsBinding::inflate) {


    private val viewModel: ProfileViewModel by viewModel()

    private lateinit var adapter: ProductPagingListAdapter
    private lateinit var  sortBottomSheet: BottomSheetDialog

    private var orderBy=Variables.sortBy[1]?:""
    private var _bottomSheetBinding: SortBottomsheetBinding?=null
    private val bottomSheetBinding get() = _bottomSheetBinding!!

    private var updatePosition:Int=-1
    private var updateValue:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapters()
        getFavProducts()
    }

    override fun setUpUI() {
        super.setUpUI()

        _bottomSheetBinding= SortBottomsheetBinding.inflate(layoutInflater)
        sortBottomSheet = createBottomSheet(bottomSheetBinding.root)
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

    override fun setUpObservers() {
        observeEvent(viewModel.favResponse,::handle)

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                //progressBar.isVisible = loadStates.refresh is LoadState.Loading
                //retry.isVisible = loadState.refresh !is LoadState.Loading
                when(loadStates.refresh)
                {
                    is LoadState.Error->{
                        hideLoadingDialog()
                        val error = loadStates.refresh as LoadState.Error
                        if (error.error is NoConnectivityException)
                        {
                            showNoConnectionDialog(this@MyFavouriteItemsFragment::onRetry)
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

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)

        adapter.update(updatePosition,updateValue)
    }


    private fun setUpAdapters() {
        adapter = ProductPagingListAdapter(requireContext())
        adapter.onItemClick = {
            val action =
                MyFavouriteItemsFragmentDirections.actionNavMyFavouriteItemsToNavSelectedProduct(
                    resources.getString(R.string.title_myFavourites),it.id
                )
            navigate(action)
        }
        adapter.onFavClick = {product,position->
            updatePosition = position
            updateValue = !product.is_favorite

            if(!product.is_favorite) {
                viewModel.addFav(product.id)
            }
            else{
                viewModel.removeFav(product.id)
            }
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

    fun onSort(option: View)
    {
        when(option.id)
        {
            bottomSheetBinding.sortByNew.id->{
                binding.sortBy.text=getString(R.string.sort_by_new)
                orderBy= Variables.sortBy[0]!!
            }
            bottomSheetBinding.sortByPopular.id->{
                binding.sortBy.text=getString(R.string.sort_by_popular)
                orderBy= Variables.sortBy[1]!!
            }
            bottomSheetBinding.sortByCheap.id->{
                binding.sortBy.text=getString(R.string.sort_by_cheap)
                orderBy= Variables.sortBy[2]!!
            }
            bottomSheetBinding.sortByExpensive.id->{
                binding.sortBy.text=getString(R.string.sort_by_expensive)
                orderBy= Variables.sortBy[3]!!
            }
        }
        sortBottomSheet.dismiss()
        getFavProducts()
    }


}