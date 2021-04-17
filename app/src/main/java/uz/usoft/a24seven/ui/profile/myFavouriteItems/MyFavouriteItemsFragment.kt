package uz.usoft.a24seven.ui.profile.myFavouriteItems

import android.os.Bundle
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
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.ui.category.selectedSubCategory.ProductPagingListAdapter
import uz.usoft.a24seven.ui.profile.ProfileViewModel
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.*

class MyFavouriteItemsFragment : BaseFragment<FragmentMyFavouriteItemsBinding>(FragmentMyFavouriteItemsBinding::inflate) {


    private val viewModel: ProfileViewModel by viewModel()

    private lateinit var adapter: ProductPagingListAdapter
    private lateinit var  sortBottomSheet: BottomSheetDialog

    private var updatePosition:Int=-1
    private var updateValue:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapters()
        getFavProducts()
    }

    private fun getFavProducts() {
        lifecycleScope.launch {
            viewModel.getFavProductsResponse().collect {
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

    }


}