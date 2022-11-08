package a24seven.uz.ui.news

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import a24seven.uz.databinding.FragmentNewsBinding
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.utils.SpacesItemDecoration
import a24seven.uz.utils.navigate
import a24seven.uz.utils.showSnackbar
import a24seven.uz.utils.toDp

class NewsFragment : BaseFragment<FragmentNewsBinding>(FragmentNewsBinding::inflate) {

    private lateinit var newsAdapter: NewsPagingListAdapter
    private val newsViewModel: NewsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
        getNews()
    }

    override fun onRetry() {
        getNews()
        mainActivity.showToolbar()
        mainActivity.showBottomNavigation()
    }

    private fun setUpAdapter() {
        newsAdapter = NewsPagingListAdapter(requireContext())
        newsAdapter.onItemClick = {
            val action = NewsFragmentDirections.actionNavNewsToSelectedNewsFragment(it.id)
            navigate(action,true)
        }
    }

    private fun getNews() {
        lifecycleScope.launch {
            newsViewModel.getNews().collect {
                newsAdapter.submitData(it)
                return@collect
            }
        }
    }

    override fun setUpRecyclers() {
        binding.newsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.newsRecycler.adapter = newsAdapter
        binding.newsRecycler.addItemDecoration(SpacesItemDecoration(toDp(16), true, 2))
    }

    override fun setUpObservers() {
        lifecycleScope.launch {
            newsAdapter.loadStateFlow.collectLatest { loadStates ->
                //progressBar.isVisible = loadStates.refresh is LoadState.Loading
                //retry.isVisible = loadState.refresh !is LoadState.Loading
                when(loadStates.refresh)
                {
                    is LoadState.Error->{
                        val error = loadStates.refresh as LoadState.Error

                        hideLoadingDialog()
                        if (error.error is NoConnectivityException)
                        {
                            showNoConnectionDialog(this@NewsFragment::onRetry)
                        }
                        else{
                            showSnackbar(error.error.message.toString())
                        }
                    }
                    is LoadState.Loading->{
                        hideNoConnectionDialog()
                        showLoadingDialog()
                    }
                    else->{
                        hideNoConnectionDialog()
                        hideLoadingDialog()
                    }
                }

            }
        }
    }
}