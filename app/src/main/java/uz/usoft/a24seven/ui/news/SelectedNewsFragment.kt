package uz.usoft.a24seven.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentSelectedNewsBinding
import uz.usoft.a24seven.network.utils.BaseFragment
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.utils.image
import uz.usoft.a24seven.utils.showSnackbar

class SelectedNewsFragment : BaseFragment() {

    private var _binding: FragmentSelectedNewsBinding? = null
    private val binding get() = _binding!!
    private val safeArgs: SelectedNewsFragmentArgs by navArgs()
    private lateinit var newsAdapter: NewsListAdapter
    private val newsViewModel: NewsViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        newsViewModel.showNews(safeArgs.newsId)
      //  setUpAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectedNewsBinding.inflate(inflater, container, false)
        return superOnCreateView(binding)
    }

    override fun onRetryClicked() {
        newsViewModel.showNews(safeArgs.newsId)
        mainActivity.showToolbar()
        mainActivity.showBottomNavigation()
    }


    override fun setUpRecyclers() {
        //  TODO("removed for now")

//        binding.otherNewsRecycler.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.otherNewsRecycler.adapter = newsAdapter
//        binding.otherNewsRecycler.addItemDecoration(SpacesItemDecoration(16, false))
    }

    override fun setUpOnClickListeners() {
      //  TODO("Not yet implemented")
    }

    override fun setUpObservers() {
        newsViewModel.showNewsResponse.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        hideNoConnectionDialog()
                        showLoadingDialog()
                    }
                    is Resource.Success -> {
                        val news=resource.data
                        binding.newsBody.text=news.content
                        binding.newsTitle.text=news.name
                        binding.newsDate.text=news.created_at
                        binding.newsImage.image(requireContext(),news.image, R.drawable.placeholder_news)
                        hideNoConnectionDialog()
                        hideLoadingDialog()
                    }
                    is Resource.GenericError -> {
                        hideLoadingDialog()
                        showSnackbar(resource.errorResponse.jsonResponse.getString("error"))
                    }
                    is Resource.Error -> {
                        hideLoadingDialog()
                        if(resource.exception is NoConnectivityException)
                            showNoConnectionDialog()
                    }
                }
            }
        })
    }

    override fun setUpPagers() {
        //TODO("Not yet implemented")
    }

    override fun setUpData() {
        //TODO("Not yet implemented")
    }

    // removed for now
//    private fun setUpAdapter() {
//        newsAdapter = NewsListAdapter(requireContext())
//        newsAdapter.onItemClick = {
//            findNavController().navigate(R.id.action_nav_selectedNews_self)
//        }
//    }
//

//    private fun setUpRecyclerView() {
//        binding.otherNewsRecycler.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.otherNewsRecycler.adapter = newsAdapter
//        binding.otherNewsRecycler.addItemDecoration(SpacesItemDecoration(16, false))
//    }
}