package uz.usoft.a24seven.ui.news

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentSelectedNewsBinding
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.getImageUri
import uz.usoft.a24seven.utils.image
import uz.usoft.a24seven.utils.intentShare
import uz.usoft.a24seven.utils.showSnackbar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

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
                        showSnackbar(resource.errorResponse.jsonResponse.getString("error"))
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