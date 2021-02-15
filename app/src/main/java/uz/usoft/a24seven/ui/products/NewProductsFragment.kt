package uz.usoft.a24seven.ui.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_new_products.*
import kotlinx.android.synthetic.main.fragment_new_products.filter
import kotlinx.android.synthetic.main.fragment_new_products.sortBy
import kotlinx.android.synthetic.main.fragment_selected_sub_category.*
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.ui.category.selectedSubCategory.SelectedSubCategoryFragmentDirections
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.createBottomSheet
import uz.usoft.a24seven.utils.toDpi

class NewProductsFragment : Fragment() {

    private lateinit var adapter: ProductsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= ProductsListAdapter(true)
        newProductsRecycler.adapter=adapter
        newProductsRecycler.layoutManager=GridLayoutManager(requireContext(),2)
        newProductsRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16)))

        adapter.onItemClick={
            val action=NewProductsFragmentDirections.actionNavNewProductsToNavSelectedProduct(resources.getString(R.string.title_newProducts))
            findNavController().navigate(action)
        }

        filter.setOnClickListener {
            (requireActivity() as MainActivity).openDrawer()
        }

        val sortBottomSheet=
                createBottomSheet(R.layout.sort_bottomsheet)
        sortBy.setOnClickListener {
            sortBottomSheet.show()
        }

    }
}