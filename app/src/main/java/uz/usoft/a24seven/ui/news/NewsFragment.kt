package uz.usoft.a24seven.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.databinding.FragmentNewsBinding
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.showSnackbar
import uz.usoft.a24seven.utils.toDp

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