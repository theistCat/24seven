package uz.usoft.a24seven.ui.category.selectedSubCategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentSelectedSubCategoryBinding
import uz.usoft.a24seven.network.utils.BaseFragment
import uz.usoft.a24seven.ui.products.ProductViewModel
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.createBottomSheet
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.toDp


class SelectedSubCategoryFragment : BaseFragment() {

    private var _binding: FragmentSelectedSubCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductPagingListAdapter
    private val safeArgs: SelectedSubCategoryFragmentArgs by navArgs()
    private val productViewModel: ProductViewModel by viewModel()
    private lateinit var sortBottomSheet: BottomSheetDialog
    private var orderBy="popular"

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectedSubCategoryBinding.inflate(inflater, container, false)
        sortBottomSheet = createBottomSheet(R.layout.sort_bottomsheet)
        return superOnCreateView(binding)

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
    }

    override fun onRetryClicked() {
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

    override fun setUpData() {
        mainActivity.setTitle(safeArgs.subCategoryName)
    }

    override fun setUpObservers() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                //progressBar.isVisible = loadStates.refresh is LoadState.Loading
                //retry.isVisible = loadState.refresh !is LoadState.Loading
                //errorMsg.isVisible = loadState.refresh is LoadState.Error
            }
        }
    }

    override fun setUpPagers() {
        //TODO("Not yet implemented")
    }
}