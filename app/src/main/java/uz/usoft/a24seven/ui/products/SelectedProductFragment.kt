package uz.usoft.a24seven.ui.products

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentSelectedProductBinding
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.utils.*

class SelectedProductFragment : BaseFragment<FragmentSelectedProductBinding>(FragmentSelectedProductBinding::inflate) {

    private lateinit var pagerAdapter: ImageCollectionAdapter
    private lateinit var similarItemAdapter: ProductsListAdapter
    private lateinit var feedbackListAdapter: FeedbackListAdapter
    private val productViewModel:ProductViewModel by viewModel()
    private val safeArgs: SelectedProductFragmentArgs by navArgs()
    private val imgList = ArrayList<String>()
    private lateinit var feedbackBottomSheet : BottomSheetDialog
    private var updateStatus=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapters()
        getProductComments()
        productViewModel.getProduct(safeArgs.productId)

    }

    private fun getProductComments() {
        lifecycleScope.launch {
            productViewModel.getProductCommentsResponse(safeArgs.productId).collect {
                feedbackListAdapter.submitData(it)
                return@collect
            }
        }
    }

    override fun setUpRecyclers() {
        binding.similarItemsRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.similarItemsRecycler.adapter = similarItemAdapter
        binding.similarItemsRecycler.addItemDecoration(SpacesItemDecoration(toDp(16), false))

        binding.feedbackRecycler.adapter = feedbackListAdapter
        binding.feedbackRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun setUpOnClickListeners() {
        binding.leaveFeedback.setOnClickListener {
            feedbackBottomSheet.show()
        }
        binding.isFavourite.setOnClickListener {
            updateStatus = if(binding.isFavourite.isChecked){
                productViewModel.addFav(safeArgs.productId)
                true
            } else{
                productViewModel.removeFav(safeArgs.productId)
                false
            }
        }
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        binding.isFavourite.isChecked=updateStatus
    }

    override fun setUpObservers() {
        observeEvent(productViewModel.favResponse,::handle)

        productViewModel.getProductResponse.observe(viewLifecycleOwner,  {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        hideNoConnectionDialog()
                        showLoadingDialog()
                    }
                    is Resource.Success -> {
                        val product = resource.data
                        binding.productName.text = product.name
                        binding.isFavourite.isChecked=product.is_favorite
                        if (product.discount_percent > 0) {
                            binding.productOldPrice.text = requireContext().getString(R.string.money_format_sum, product.price)
                            binding.productPrice.text = requireContext().getString(R.string.money_format_sum, product.price_discount)
                        } else {
                            binding.productPrice.text  = requireContext().getString(R.string.money_format_sum, product.price)
                            binding.productOldPrice.visibility=View.INVISIBLE
                            binding.discountTag.visibility=View.INVISIBLE

                        }

                        product.products_related?.let { similarItems ->
                            similarItemAdapter.updateList(similarItems)
                        }
                        hideNoConnectionDialog()
                        hideLoadingDialog()

                    }
                    is Resource.GenericError -> {
                        hideLoadingDialog()
                        showSnackbar(resource.errorResponse.message)
                    }
                    is Resource.Error -> {
                        hideLoadingDialog()
                        if(resource.exception is NoConnectivityException)
                            showNoConnectionDialog(this::onRetry)
                        else resource.exception.message?.let { it1 -> showSnackbar(it1) }
                    }
                }
            }
        })
    }

    override fun setUpPagers() {
        imgList.add("https://i.imgur.com/0Qy.png")
        imgList.add("https://i.imgur.com/0Qy.png")
        imgList.add("https://i.imgur.com/0Qy.png")
        pagerAdapter = ImageCollectionAdapter(this)
        pagerAdapter.updateImageList(imgList)
        setUpViewPager(pagerAdapter, binding.productPager, binding.productTabLayout)
    }

    override fun onRetry() {
        getProductComments()
        productViewModel.getProduct(safeArgs.productId)
        mainActivity.showToolbar()
        mainActivity.showBottomNavigation()
    }

    private fun setUpAdapters() {
        similarItemAdapter = ProductsListAdapter(requireContext())
        feedbackListAdapter = FeedbackListAdapter()
    }


    override fun setUpUI() {
        mainActivity.setTitle(safeArgs.selectedCategoryName)
    }
}