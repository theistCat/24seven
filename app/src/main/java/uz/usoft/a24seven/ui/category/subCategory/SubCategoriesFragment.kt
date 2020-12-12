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


class SubCategoriesFragment : Fragment() {

    private lateinit var adapter: SubCategoriesListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= SubCategoriesListAdapter()
        subCategoryRecycler.adapter=adapter
        subCategoryRecycler.layoutManager=LinearLayoutManager(requireContext())


        val safeArgs:SubCategoriesFragmentArgs by navArgs()
        (requireActivity() as MainActivity).main_toolbar.title=safeArgs.categoryName

        adapter.onItemClick={
            val subCategoryName=it.name
            val action=SubCategoriesFragmentDirections.actionNavSubCategoriesToNavSelectedSubCategory(subCategoryName)
            findNavController().navigate(action)
        }
    }
}