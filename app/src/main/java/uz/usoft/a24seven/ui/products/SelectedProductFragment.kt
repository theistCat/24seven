package uz.usoft.a24seven.ui.products

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.databinding.ChangeLanguageBottomsheetBinding
import uz.usoft.a24seven.databinding.FeedbackBottomsheetBinding
import uz.usoft.a24seven.databinding.FragmentSelectedProductBinding
import uz.usoft.a24seven.network.models.CartItem
import uz.usoft.a24seven.network.models.Characteristics
import uz.usoft.a24seven.network.models.Comment
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.network.models.Unit
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.utils.*

class SelectedProductFragment : BaseFragment<FragmentSelectedProductBinding>(FragmentSelectedProductBinding::inflate) {

    private lateinit var pagerAdapter: ImagesAdapter
    private lateinit var similarItemAdapter: ProductsListAdapter
    private lateinit var feedbackListAdapter: FeedbackListAdapter
    private lateinit var characteristicsListAdapter: CharacteristicsListAdapter
    private val productViewModel:ProductViewModel by viewModel()
    private val safeArgs: SelectedProductFragmentArgs by navArgs()
    private val imgList = ArrayList<String>()
    private lateinit var feedbackBottomSheet : BottomSheetDialog
    private var _feedbackBottomSheetBinding : FeedbackBottomsheetBinding?=null
    private val feedbackBottomSheetBinding get() = _feedbackBottomSheetBinding!!
    private var updateStatus=false
    private var count=1
    private var unit:Unit?=null
    private var inCart=false

    private var updateId=-1
    private var updateValue=false
    private var addSimilarToCart=false
    private var addSimilarToCartId=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapters()
        getProductComments()

        checkItem()
        productViewModel.getProduct(safeArgs.productId)

    }


    private fun checkItem() {
        lifecycleScope.launch {
            productViewModel.checkItem(safeArgs.productId)
        }
    }

    private fun update() {
        lifecycleScope.launch {
            productViewModel.updateCart(CartItem(safeArgs.productId,count))
        }
    }

    private fun remove() {
        lifecycleScope.launch {
            productViewModel.deleteItem(CartItem(safeArgs.productId,count))
        }
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


        binding.characteristicsRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.characteristicsRecycler.adapter = characteristicsListAdapter
        binding.characteristicsRecycler.addItemDecoration(SpacesItemDecoration(toDp(16), true,span = 1))

        binding.feedbackRecycler.adapter = feedbackListAdapter
        binding.feedbackRecycler.layoutManager = LinearLayoutManager(requireContext())

    }

    override fun setUpOnClickListeners() {
        binding.leaveFeedback.setOnClickListener {
            feedbackBottomSheet.show()
        }

        binding.addProductToCart.setOnClickListener {

            productViewModel.storeCart(CartItem(safeArgs.productId,count))
           // productViewModel.addToCartWithoutEmit(CartItem(safeArgs.productId,count))
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


        feedbackBottomSheetBinding.sendFeedback.setOnClickListener {
            if(feedbackBottomSheetBinding.feedback.text.isNotBlank())
            {
                productViewModel.addComment(safeArgs.productId,PrefManager.getName(requireContext()),feedbackBottomSheetBinding.feedback.text.toString())
            }
        }

        binding.inc.setOnClickListener {
            count++
            if(inCart)
                update()
            else
                binding.count.text =
                    //getString(R.string.count_with_unit, (count*(unit?.count?:1.0)), unit?.name)
                    (count*(unit?.count?:1.0)).toString()
        }

        binding.dec.setOnClickListener {
            if(count>1) {
                count--
                if(inCart)
                    update()
                else
                    binding.count.text =
                        //getString(R.string.count_with_unit, (count*(unit?.count?:1.0)), unit?.name)
                        (count*(unit?.count?:1.0)).toString()
            }
        }

        binding.remove.setOnClickListener {
            remove()
            showLoadingDialog()
        }
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        binding.isFavourite.isChecked=updateStatus

        if(data is Comment)
        {
            feedbackBottomSheet.dismiss()
            showSnackbar(getString(R.string.tour_comment_in_review),Snackbar.LENGTH_LONG)
        }
        else{
            showSnackbar(getString(R.string.success))
            similarItemAdapter.update(updateId,updateValue)
        }
    }

    override fun setUpObservers() {

        productViewModel.updateCartResponse.observe(
            viewLifecycleOwner, Observer { result ->
                result?.let {

                        result.getContentIfNotHandled()?.let { resource ->
                            when (resource) {
                                is Resource.Loading -> {
                                    onLoading()
                                }
                                is Resource.Success -> {
                                    //viewModel.st(productsList)
                                    hideLoadingDialog()
                                    binding.count.text = getString(R.string.count_with_unit, (count*(unit?.count?:1.0)), unit?.name)
                                    if (count > 1) {
                                        binding.dec.visibility = View.VISIBLE
                                        binding.remove.visibility = View.GONE
                                    }
                                    else if(inCart)
                                    {
                                        binding.dec.visibility=View.GONE
                                        binding.remove.visibility=View.VISIBLE
                                    }
                                }
                                is Resource.GenericError -> {
                                    onGenericError(resource)
                                }
                                is Resource.Error -> {
                                    onError(resource)
                                }
                            }
                        }
                }
            }
        )

        productViewModel.deleteItem.observe(
            viewLifecycleOwner, Observer { result ->
                result?.let {
                    result.getContentIfNotHandled()?.let { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                onLoading()
                            }
                            is Resource.Success -> {
                                //viewModel.st(productsList)
                                hideLoadingDialog()
                                inCart=false
                                count=1
                                binding.count.text = getString(R.string.count_with_unit, (count*(unit?.count?:1.0)), unit?.name)

                                binding.dec.visibility = View.VISIBLE
                                binding.remove.visibility = View.GONE

                                binding.addProductToCart.isEnabled=true
                                binding.addProductToCart.text=getString(R.string.add_to_cart)


                            }
                            is Resource.GenericError -> {
                                onGenericError(resource)
                            }
                            is Resource.Error -> {
                                onError(resource)
                            }
                        }
                    }
                }
            }
        )

        productViewModel.addToCartResponseTwo.observe(
            viewLifecycleOwner, Observer { result->
                result?.let {
                    checkItem()
                     if(it.toInt()==-1)
                     {
                         showActionSnackbar("Already in cart","replace",{
                             productViewModel.addToCartWithoutEmit(CartItem(safeArgs.productId,count),replace = true)
                         },Snackbar.LENGTH_LONG)
                     }
                    else{
                         if(addSimilarToCart)
                         {
                             PrefManager.getInstance(requireContext()).edit().putBoolean(addSimilarToCartId.toString(),true).apply()
                             addSimilarToCart=false
                         }
                         else
                            PrefManager.getInstance(requireContext()).edit().putBoolean(safeArgs.productId.toString(),true).apply()

                     }
                }
            }
        )

        observe(productViewModel.removeFromCartResponse) {
            if(it!=0)
            {
                PrefManager.getInstance(requireContext()).edit().remove(safeArgs.productId.toString()).apply()
                hideLoadingDialog()
                inCart=false
                binding.dec.visibility=View.VISIBLE
                binding.remove.visibility=View.GONE
                binding.addProductToCart.isEnabled=true
                binding.addProductToCart.text=getString(R.string.add_to_cart)
            }
        }

        productViewModel.checkItemResponse.observe(
            viewLifecycleOwner, Observer { result ->
//                if (result == null) {
//                    inCart=false
//                    binding.addProductToCart.isEnabled=true
//                    binding.addProductToCart.text=getString(R.string.add_to_cart)
//                }
//                result?.let {
//                    inCart = true
//                    count=it.count
//                    binding.addProductToCart.isEnabled = false
//                    binding.addProductToCart.text = getString(R.string.in_cart)
//                    if (count > 1) {
//                        binding.dec.visibility = View.VISIBLE
//                        binding.remove.visibility = View.GONE
//                    }
//                    else if(inCart)
//                    {
//                        binding.dec.visibility=View.GONE
//                        binding.remove.visibility=View.VISIBLE
//                    }
//                }
            }
        )

        observeEvent(productViewModel.favResponse,::handle)
        observeEvent(productViewModel.addCommentResponse,::handle)

        observeEvent(productViewModel.storeCartResponse,::handleAddItem)
        //observeEvent(productViewModel.addToCartResponse,::handle)

        productViewModel.getProductResponse.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        hideNoConnectionDialog()
                        showLoadingDialog()
                    }
                    is Resource.Success -> {
                        val product = resource.data
                        binding.productName.text = product.name
                        binding.isFavourite.isChecked = product.is_favorite

                        if (product.description != "null") {
                            binding.productDetail.text = HtmlCompat.fromHtml(
                                product.description ?: "",
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                            )
                        }
                        else binding.productDetail.visibility=View.GONE


                        if (product.cart_count>0)
                            count=product.cart_count


                        unit = product.unit
                        binding.count.text =
                            getString(R.string.count_with_unit, (count * product.unit.count), unit?.name )
                            //(count*(unit?.count?:1.0)).toString()

                        binding.characteristics.isVisible =
                            product.characteristics?.isNotEmpty() == true
                        characteristicsListAdapter.updateList(product.characteristics as ArrayList<Characteristics>)

                        if (product.discount_percent > 0) {
                            binding.discountTag.text =
                                getString(R.string.discount, product.discount_percent)
                            binding.productOldPrice.text =
                                requireContext().getString(R.string.money_format_sum, product.price)
                            binding.productPrice.text =
                                //requireContext().getString(R.string.money_format_sum, product.price_discount)
                                requireContext().getString(R.string.money_format_sum_unit_two_lines, product.price_discount, product.unit.name)
                        } else {
                            binding.productPrice.text =
                                //requireContext().getString(R.string.money_format_sum, product.price)
                                requireContext().getString(R.string.money_format_sum_unit_two_lines, product.price, product.unit.name)

                            binding.productOldPrice.visibility = View.INVISIBLE
                            binding.discountTag.visibility = View.INVISIBLE

                        }

                        inCart=product.is_cart
                        if (!product.images.isNullOrEmpty()) {
                            imgList.clear()
                            for (images in product.images) {
                                imgList.add(images.path)
                            }
                            pagerAdapter.updateList(imgList)
                        }

                        product.products_related?.let { similarItems ->
                            similarItemAdapter.updateList(similarItems)
                        }

                        if( product.product_count==0)
                        {
                            binding.addProductToCart.isEnabled=false
                            binding.addProductToCart.text=getString(R.string.out_of_stock)
                        }


                        if(product.is_cart)
                        {
                            binding.addProductToCart.isEnabled = false
                            binding.addProductToCart.text = getString(R.string.in_cart)
                        }

                        if (product.cart_count > 1) {
                            binding.dec.visibility = View.VISIBLE
                            binding.remove.visibility = View.GONE
                        }
                        else if(product.is_cart)
                        {
                            binding.dec.visibility=View.GONE
                            binding.remove.visibility=View.VISIBLE
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
                        if (resource.exception is NoConnectivityException)
                            showNoConnectionDialog(this::onRetry)
                        else resource.exception.message?.let { it1 -> showSnackbar(it1) }
                    }
                }
            }
        })
    }

    override fun setUpPagers() {
        pagerAdapter = ImagesAdapter(requireContext())
        imgList.add("https://i.imgur.com/0Qy.png")
        pagerAdapter.updateList(imgList)
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
        similarItemAdapter.onItemClick={
            val action= SelectedProductFragmentDirections.actionNavSelectedProductSelf(safeArgs.selectedCategoryName,it.id)
            navigate(action)

        }
        similarItemAdapter.addFav={product->
            updateId=product.id
            updateValue=true
            productViewModel.addFav(product.id)

        }

        similarItemAdapter.removeFav={product->
            updateId=product.id
            updateValue=false
            productViewModel.removeFav(product.id)

        }

        similarItemAdapter.addToCart={product->
            addSimilarToCart=true
            addSimilarToCartId=product.id
            productViewModel.storeCart(CartItem(product.id,1))
        }


        feedbackListAdapter = FeedbackListAdapter()
        characteristicsListAdapter= CharacteristicsListAdapter()
    }


    override fun setUpUI() {
        mainActivity.setTitle(safeArgs.selectedCategoryName)
        _feedbackBottomSheetBinding= FeedbackBottomsheetBinding.inflate(layoutInflater)
        feedbackBottomSheet=createBottomSheet(feedbackBottomSheetBinding.root)

    }

    override fun onPause() {
        super.onPause()
        feedbackBottomSheet.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        _feedbackBottomSheetBinding=null
    }


    private fun handleAddItem(event: Event<Resource<Any>>){
        if(addSimilarToCart)
        {
            PrefManager.getInstance(requireContext()).edit().putBoolean(addSimilarToCartId.toString(),true).apply()
            similarItemAdapter.getItem(addSimilarToCartId)?.is_cart=true
            addSimilarToCart=false
        }
        else
        {
        event.getContentIfNotHandled()?.let { resource ->
            when (resource) {
                is Resource.Loading -> {
                    onLoading()
                }
                is Resource.Success -> {
                    //viewModel.st(productsList)
                    hideLoadingDialog()
                    inCart = true
                    binding.addProductToCart.isEnabled = false
                    binding.addProductToCart.text = getString(R.string.in_cart)
                    binding.count.text = getString(
                        R.string.count_with_unit,
                        (count * (unit?.count ?: 1.0)),
                        unit?.name
                    )

                    if (count > 1) {
                        binding.dec.visibility = View.VISIBLE
                        binding.remove.visibility = View.GONE
                    } else if (inCart) {
                        binding.dec.visibility = View.GONE
                        binding.remove.visibility = View.VISIBLE
                    }


                }
                is Resource.GenericError -> {
                    onGenericError(resource)
                }
                is Resource.Error -> {
                    onError(resource)
                }
            }
        }
        }
    }
}