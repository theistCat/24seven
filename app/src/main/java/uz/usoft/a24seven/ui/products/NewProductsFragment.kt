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
import uz.usoft.a24seven.databinding.FragmentNewProductsBinding
import uz.usoft.a24seven.databinding.FragmentNewsBinding
import uz.usoft.a24seven.databinding.FragmentSelectedNewsBinding
import uz.usoft.a24seven.ui.category.selectedSubCategory.SelectedSubCategoryFragmentDirections
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.createBottomSheet
import uz.usoft.a24seven.utils.toDpi

class NewProductsFragment : Fragment() {

    private var _binding: FragmentNewProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductsListAdapter
    private val sortBottomSheet = createBottomSheet(R.layout.sort_bottomsheet)

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
        _binding = FragmentNewProductsBinding.inflate(inflater, container, false)
        setUpRecycler()
        setUpOnClickListener()
        return binding.root
    }

    private fun setUpAdapter() {
        adapter = ProductsListAdapter(requireContext(),isGrid = true)
        adapter.onItemClick = {
            val action = NewProductsFragmentDirections.actionNavNewProductsToNavSelectedProduct(
                resources.getString(R.string.title_newProducts)
            )
            findNavController().navigate(action)
        }
    }

    private fun setUpRecycler() {
        newProductsRecycler.adapter = adapter
        newProductsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        newProductsRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16)))

    }

    private fun setUpOnClickListener() {
        binding.filter.setOnClickListener {
            (requireActivity() as MainActivity).openDrawer()
        }
        binding.sortBy.setOnClickListener {
            sortBottomSheet.show()
        }
    }
}