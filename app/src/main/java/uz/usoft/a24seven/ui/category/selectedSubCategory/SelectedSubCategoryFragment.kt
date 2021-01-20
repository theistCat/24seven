package uz.usoft.a24seven.ui.category.selectedSubCategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_selected_sub_category.*
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.createBottomSheet


class SelectedSubCategoryFragment : Fragment() {

    private lateinit var adapter: ProductsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selected_sub_category, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs:SelectedSubCategoryFragmentArgs by navArgs()
        val mainActivity = (requireActivity() as MainActivity)
        mainActivity.main_toolbar.title=safeArgs.subCategoryName


        adapter= ProductsListAdapter(true)
        selectedSubCategoryRecycler.adapter=adapter
        selectedSubCategoryRecycler.layoutManager= GridLayoutManager(requireContext(),2)
        val density=requireContext().resources.displayMetrics.density
        selectedSubCategoryRecycler.addItemDecoration(SpacesItemDecoration((16*density+0.5f).toInt()))


        adapter.onItemClick={
            val action=SelectedSubCategoryFragmentDirections.actionNavSelectedSubCategoryToNavSelectedProduct(safeArgs.subCategoryName)
            findNavController().navigate(action)
        }

        filter.setOnClickListener {
            mainActivity.openDrawer()
        }

        val sortBottomSheet=
            createBottomSheet(R.layout.sort_bottomsheet)
        sortBy.setOnClickListener {
            sortBottomSheet.show()
        }
    }
}