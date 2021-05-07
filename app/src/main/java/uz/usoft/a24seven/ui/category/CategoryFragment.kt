package uz.usoft.a24seven.ui.category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_category.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentCategoryBinding
import uz.usoft.a24seven.network.models.CategoryObject
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.toDpi

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CategoryViewModel by viewModel()
    private lateinit var adapter: CategoriesListAdapter
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpAdapter()
        viewModel.getCategories()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        fetchCategoryResponse()
        return binding.root
    }

    private fun fetchCategoryResponse() {
        viewModel.getCategoriesResponse.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled().let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        adapter.updateList(resource.data)
                        Log.d("categories", "${resource.data}")
                    }
                    is Resource.Error -> {
                        Log.d("Error", "${resource.exception.message}")
                    }
                    is Resource.GenericError -> {
                        Log.d("Error", resource.errorResponse.message)

                    }
                    else -> {
                    }
                }
            }
        })
    }

    private fun setUpAdapter() {
        adapter = CategoriesListAdapter(requireContext())
        adapter.onItemClick = {
            val categoryName = it.name
            val action =
                CategoryFragmentDirections.actionNavCategoriesToSubCategoriesFragment()
            findNavController().navigate(action)
        }
    }

    private fun setUpRecyclerView() {
        binding.categoryRecycler.adapter = adapter
        binding.categoryRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.categoryRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16), true, 1))
    }
}