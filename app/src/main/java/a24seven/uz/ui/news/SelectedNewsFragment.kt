package a24seven.uz.ui.news

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import org.koin.androidx.viewmodel.ext.android.viewModel
import a24seven.uz.R
import a24seven.uz.databinding.FragmentSelectedNewsBinding
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.network.utils.Resource
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.getImageUri
import a24seven.uz.utils.image
import a24seven.uz.utils.intentShare
import a24seven.uz.utils.showSnackbar

class SelectedNewsFragment : BaseFragment<FragmentSelectedNewsBinding>(FragmentSelectedNewsBinding::inflate) {

    private val safeArgs: SelectedNewsFragmentArgs by navArgs()
    private lateinit var newsAdapter: NewsListAdapter
    private val newsViewModel: NewsViewModel by viewModel()
    private var link=""
    lateinit var bitmap:Bitmap
    private var uri:Uri?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        newsViewModel.showNews(safeArgs.newsId)
      //  setUpAdapter()
    }


    override fun onRetry() {
        newsViewModel.showNews(safeArgs.newsId)
        mainActivity.showToolbar()
        mainActivity.showBottomNavigation()
    }

    override fun onResume() {
        super.onResume()
        mainActivity.showBottomNavigation()
    }

    override fun setUpOnClickListeners() {

        binding.shareFacebook.setOnClickListener { intentShare(link, 2,uri) }
        binding.shareInstagram.setOnClickListener { intentShare(link, 1,uri)  }
        binding.shareTelegram.setOnClickListener { intentShare(link, 0,uri) }
    }


    override fun setUpRecyclers() {
        //  TODO("removed for now")

//        binding.otherNewsRecycler.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.otherNewsRecycler.adapter = newsAdapter
//        binding.otherNewsRecycler.addItemDecoration(SpacesItemDecoration(16, false))
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
                        val news = resource.data
                        binding.newsBody.text = news.content
                        binding.newsTitle.text = news.name
                        binding.newsDate.text = news.created_at
                        link = news.url ?: ""
                        binding.newsImage.image(
                            requireContext(),
                            news.image,
                            R.drawable.placeholder_news
                        )

                        Glide.with(requireContext()).asBitmap()
                            .load(news.image)
                            .into(object : CustomTarget<Bitmap>(
                                Target.SIZE_ORIGINAL,
                                Target.SIZE_ORIGINAL
                            ){
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    bitmap = resource
                                    uri= getImageUri(bitmap)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                }

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    super.onLoadFailed(errorDrawable)
                                    Toast.makeText(requireContext(),getString(R.string.somethign_went_wrong),Toast.LENGTH_SHORT).show()

                                }

                            } )
                        hideNoConnectionDialog()
                        hideLoadingDialog()
                    }
                    is Resource.GenericError -> {
                        hideLoadingDialog()
                        showSnackbar(resource.errorResponse.jsonResponse.optString("error")?:"error")
                    }
                    is Resource.Error -> {
                        hideLoadingDialog()
                        if (resource.exception is NoConnectivityException)
                            showNoConnectionDialog(this::onRetry)
                        else resource.exception.message?.let { it1 -> showSnackbar(it1) }
                    }

                }
            }
        })
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