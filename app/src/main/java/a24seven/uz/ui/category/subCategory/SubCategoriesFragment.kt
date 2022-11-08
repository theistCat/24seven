package a24seven.uz.ui.category.subCategory

import android.os.Bundle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import a24seven.uz.databinding.FragmentSubCategoriesBinding
import a24seven.uz.network.models.CategoryObject
import a24seven.uz.network.models.SubCategoriesObject
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.navigate


class SubCategoriesFragment : BaseFragment<FragmentSubCategoriesBinding>(FragmentSubCategoriesBinding::inflate) {

    private lateinit var adapter: SubCategoriesListAdapter

    val safeArgs: SubCategoriesFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
    }


    private fun setUpAdapter() {
        adapter = SubCategoriesListAdapter()
        adapter.onItemClick = {
            val subCategoryName = it.name
            val subcategories = SubCategoriesObject(it.parents as ArrayList<CategoryObject>)

            val action =
                if(it.parents.isEmpty())
                    SubCategoriesFragmentDirections.actionNavSubCategoriesToNavSelectedSubCategory(subCategoryName = subCategoryName, subCategoryId = it.id)
                else
                    SubCategoriesFragmentDirections.actionNavSubCategoriesSelf(subcategories, subCategoryName)

            navigate(action)
        }
    }


    override fun setUpUI() {
        setTitle(safeArgs.categoryName)
        adapter.updateList(safeArgs.subCategories.subcategories)

    }

    override fun onResume() {
        super.onResume()
        setTitle(safeArgs.categoryName)
    }

    override fun setUpRecyclers() {
        binding.subCategoryRecycler.adapter = adapter
        binding.subCategoryRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

}