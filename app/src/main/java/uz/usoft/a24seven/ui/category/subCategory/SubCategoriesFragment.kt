package uz.usoft.a24seven.ui.category.subCategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sub_categories.*
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentSubCategoriesBinding
import uz.usoft.a24seven.network.models.CategoryObject
import uz.usoft.a24seven.network.models.SubCategoriesObject
import uz.usoft.a24seven.utils.navigate


class SubCategoriesFragment : Fragment() {

    private var _binding: FragmentSubCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SubCategoriesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSubCategoriesBinding.inflate(inflater, container, false)
        setUpRecycler()
        setUpData()
        return binding.root
    }

    private fun setUpAdapter() {
        adapter = SubCategoriesListAdapter()
        adapter.onItemClick = {
            val subCategoryName = it.name
            val action =
                if(it.parents.isEmpty())
                SubCategoriesFragmentDirections.actionNavSubCategoriesToNavSelectedSubCategory(
                    subCategoryName
                )
                else{
                    SubCategoriesFragmentDirections.actionNavSubCategoriesSelf(
                        subCategoryName,
                        SubCategoriesObject(it.parents as ArrayList<CategoryObject>)
                    )
                }
            navigate(action)
        }
    }

    private fun setUpRecycler() {
        binding.subCategoryRecycler.adapter = adapter
        binding.subCategoryRecycler.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun setUpData() {
//        val safeArgs: SubCategoriesFragmentArgs by navArgs()
//        (requireActivity() as MainActivity).main_toolbar.title = safeArgs.categoryName

    }


}