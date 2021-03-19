package uz.usoft.a24seven.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentNewsBinding
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.toDp

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: NewsPagingListAdapter
    private val newsViewModel: NewsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpOnAdapter()
        getNews()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        return binding.root
    }

    private fun setUpOnAdapter() {
        newsAdapter = NewsPagingListAdapter(requireContext())
        newsAdapter.onItemClick = {
            val action = NewsFragmentDirections.actionNavNewsToSelectedNewsFragment(it.id)
            navigate(action,true)
        }
    }

    private fun setUpRecyclerView() {
        binding.newsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.newsRecycler.adapter = newsAdapter
        binding.newsRecycler.addItemDecoration(SpacesItemDecoration(toDp(16), true, 2))
    }

    private fun getNews() {
        lifecycleScope.launch {
            newsViewModel.getNews().collect {
                newsAdapter.submitData(it)
                return@collect
            }
        }
    }

}