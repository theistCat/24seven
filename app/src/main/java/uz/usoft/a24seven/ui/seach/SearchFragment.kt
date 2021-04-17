package uz.usoft.a24seven.ui.seach

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentSearchBinding
import uz.usoft.a24seven.ui.category.selectedSubCategory.ProductPagingListAdapter
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.toDp

class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private lateinit var adapter: ProductPagingListAdapter
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpAdapter()
    }

    private fun setUpAdapter() {
        adapter = ProductPagingListAdapter(requireContext())
        adapter.onItemClick = {
                val action=SearchFragmentDirections.actionNavSearchToNavSelectedProduct("",it.id)
                navigate(action)
        }
    }

    private fun search(query:String) {
        lifecycleScope.launch {
            viewModel.getSearchResponse(query).collect {
                adapter.submitData(it)
                return@collect
            }
        }
    }

    override fun setUpUI() {
        super.setUpUI()

        binding.searchQuery.requestFocus()
    }

    override fun setUpRecyclers() {
        binding.searchResultsRecycler.adapter = adapter
        binding.searchResultsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.searchResultsRecycler.addItemDecoration(SpacesItemDecoration(toDp(16)))
    }


    override fun setUpOnClickListeners() {

        binding.searchQuery.doAfterTextChanged {
            if (it!!.isNotBlank())
                search(it.toString())
        }
    }


}