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
import uz.usoft.a24seven.databinding.FragmentSelectedSubCategoryBinding
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.createBottomSheet
import uz.usoft.a24seven.utils.toDpi


class SelectedSubCategoryFragment : Fragment() {

    private var _binding: FragmentSelectedSubCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductsListAdapter
    private val safeArgs: SelectedSubCategoryFragmentArgs by navArgs()
    private val mainActivity = (requireActivity() as MainActivity)
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
        _binding = FragmentSelectedSubCategoryBinding.inflate(inflater, container, false)
        setUpRecycler()
        setUpData()
        setUpClickListener()
        return binding.root

    }


    private fun setUpAdapter() {
        adapter = ProductsListAdapter(true)
        adapter.onItemClick = {
            val action =
                SelectedSubCategoryFragmentDirections.actionNavSelectedSubCategoryToNavSelectedProduct(
                    safeArgs.subCategoryName
                )
            findNavController().navigate(action)
        }

    }

    private fun setUpRecycler() {
        binding.selectedSubCategoryRecycler.adapter = adapter
        binding.selectedSubCategoryRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.selectedSubCategoryRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16)))

    }

    private fun setUpData() {
        mainActivity.main_toolbar.title = safeArgs.subCategoryName
    }

    private fun setUpClickListener() {
        binding.filter.setOnClickListener {
            mainActivity.openDrawer()
        }
        binding.sortBy.setOnClickListener {
            sortBottomSheet.show()
        }
    }
}