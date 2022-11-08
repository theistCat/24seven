package a24seven.uz.ui.cart

import a24seven.uz.R
import a24seven.uz.data.PrefManager
import a24seven.uz.databinding.FragmentCartBinding
import a24seven.uz.network.models.CartItem
import a24seven.uz.network.models.CartResponse
import a24seven.uz.network.models.CheckOutData
import a24seven.uz.network.models.Product
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.Resource
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.SpacesItemDecoration
import a24seven.uz.utils.navigate
import a24seven.uz.utils.observe
import a24seven.uz.utils.observeEvent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel


class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {

    private lateinit var adapter: CartItemListAdapter
    private val viewModel: CartViewModel by viewModel()
    private val productsList = HashMap<String, Int>()
    private val products = HashMap<String, Int>()
    private lateinit var checkOutData: CheckOutData
    private var productId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//        }
        setUpAdapter()

        viewModel.getCart(productsList)
    }

    override fun setUpUI() {
        super.setUpUI()

        setTitle("Корзина")
//        mainActivity.showBottomNavigation()
    }

    private fun setUpAdapter() {
        adapter = CartItemListAdapter(requireContext())

        adapter.remove = {
            productId = it.id

            viewModel.remove(CartItem(it.id, it.count))
        }

        adapter.updateCart = { it, inc ->
            if (it.count > 1 || inc)
                viewModel.updateCart(CartItem(it.id, if (inc) it.count + 1 else it.count - 1))
        }
    }

    override fun setUpRecyclers() {
        binding.cartRecycler.adapter = adapter
        binding.cartRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecycler.addItemDecoration(
            SpacesItemDecoration(
                (requireContext().resources.displayMetrics.density * 16 + 0.5f).toInt(),
                true,
                1
            )
        )
    }

    override fun setUpOnClickListeners() {
        binding.checkout.setOnClickListener {
            val action = CartFragmentDirections.actionNavCartToNavCheckOut(
                checkOutData,
                null,
                null,
                null,
                null
            )
            navigate(action)
        }
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        if (data is CartResponse) {
            data as CartResponse
//        adapter.updateList(data.products as ArrayList<Product>)
//        adapter.updateItems(viewModel.cart.value!!)
//        binding.totalPrice.text = getString(R.string.money_format_sum, data.total)
//        for (i in adapter.productsList!!.indices) {
//            products["products[$i][id]"] = adapter.productsList!![i].id
//            products["products[$i][count]"] = adapter.productsList!![i].count
//        }
//        checkOutData = CheckOutData(
//            products,
//            data.total,
//            data.total + data.delivery_price,
//            data.delivery_price
//        )
//
//        binding.checkout.isEnabled = adapter.itemCount != 0


            val productsList = ArrayList<Product>()
            val cartItems = ArrayList<CartItem>()

            data.products.forEach { cartItemImpl ->
                productsList.add(cartItemImpl.product)
                cartItems.add(CartItem(cartItemImpl.product.id, cartItemImpl.count))
            }
            adapter.updateList(productsList)
            adapter.updateItems(cartItems)

            binding.cartEmpty.isVisible = productsList.isEmpty()
            binding.textView.isVisible = productsList.isNotEmpty()
            binding.totalPrice.isVisible = productsList.isNotEmpty()
            binding.checkout.isVisible = productsList.isNotEmpty()


            binding.totalPrice.text = getString(R.string.money_format_sum, data.total)

            for (i in adapter.productsList!!.indices) {
                products["products[$i][id]"] = adapter.productsList!![i].id
                products["products[$i][count]"] = adapter.productsList!![i].count
            }

            checkOutData = CheckOutData(
                products,
                data.total,
                data.total + data.delivery_price,
                data.delivery_price
            )

            binding.checkout.isEnabled = adapter.itemCount != 0
        }
    }

    override fun onRetry() {
        super.onRetry()

        mainActivity.showBottomNavigation()
//        mainActivity.hideToolbar()
        viewModel.getCart(productsList)
    }

    override fun setUpObservers() {

        viewModel.updateCartResponse.observe(
            viewLifecycleOwner, Observer { products ->
                products?.let {
                    products.getContentIfNotHandled()?.let { resource ->
                        when (resource) {
                            is Resource.Loading -> {
                                // onLoading()
                            }

                            is Resource.Success -> {
                                viewModel.getCart(productsList)
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

        observe(viewModel.removeFromCartResponse) {
            if (it != 0 && productId != -1) {
                PrefManager.getInstance(requireContext()).edit().remove(productId.toString())
                    .apply()
            }
        }

        observeEvent(viewModel.cartResponse, ::handle)

        observeEvent(viewModel.removeCart, ::handleRemoveItem)
    }

    private fun handleRemoveItem(event: Event<Resource<Any>>) {
        event.getContentIfNotHandled()?.let { resource ->
            when (resource) {
                is Resource.Loading -> {
                    onLoading()
                }

                is Resource.Success -> {
                    viewModel.getCart(productsList)
                    if (productId != -1) {
                        PrefManager.getInstance(requireContext()).edit()
                            .remove(productId.toString())
                            .apply()
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