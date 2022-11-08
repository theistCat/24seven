package a24seven.uz.ui.category

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import a24seven.uz.databinding.FragmentCategoryBinding
import a24seven.uz.network.models.CategoryObject
import a24seven.uz.network.models.SubCategoriesObject
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.SpacesItemDecoration
import a24seven.uz.utils.navigate
import a24seven.uz.utils.observeEvent
import a24seven.uz.utils.toDp

class CategoryFragment : BaseFragment<FragmentCategoryBinding>(FragmentCategoryBinding::inflate) {

    private lateinit var adapter: CategoriesListAdapter
    private val categoriesViewModel: CategoriesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpAdapter()
        categoriesViewModel.getCategoriesResponse()

    }

    override fun onRetry() {
        categoriesViewModel.getCategoriesResponse()
        mainActivity.showToolbar()
        mainActivity.showBottomNavigation()
    }

    private fun setUpAdapter() {
        adapter = CategoriesListAdapter(requireContext())
        adapter.onItemClick = {
            val categoryName = it.name
            val subcategories = SubCategoriesObject(it.parents as ArrayList<CategoryObject>)
            val action = CategoryFragmentDirections.actionNavCategoriesToSubCategoriesFragment(subcategories, categoryName)
            navigate(action)
        }
    }

    override fun setUpRecyclers() {
        binding.categoryRecycler.adapter = adapter
        binding.categoryRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.categoryRecycler.addItemDecoration(SpacesItemDecoration(toDp(16), true, 1))
    }

    override fun <T : Any> onSuccess(data: T) {
        data as List<CategoryObject>
        adapter.updateList(data)
        hideLoadingDialog()
    }

    override fun setUpObservers() {
        observeEvent(categoriesViewModel.getCategoriesResponse,::handle)
    }
}
