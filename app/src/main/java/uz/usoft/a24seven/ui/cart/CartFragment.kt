package uz.usoft.a24seven.ui.cart

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentCartBinding
import uz.usoft.a24seven.network.models.CartItem
import uz.usoft.a24seven.network.models.CartResponse
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.observeEvent


class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {

    private lateinit var adapter: CartItemListAdapter
    private val viewModel: CartViewModel by viewModel()
    private val productsList=HashMap<String,Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
    }

    private fun setUpAdapter() {
        adapter = CartItemListAdapter(requireContext())

        adapter.remove={
            viewModel.remove(CartItem(it.id,it.count))
        }

        adapter.updateCart={ it, inc->
            if (it.count>1||inc)
                viewModel.update(CartItem(it.id,if(inc)it.count+1 else it.count-1))
        }
    }

    override fun setUpRecyclers() {
        binding.cartRecycler.adapter = adapter
        binding.cartRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecycler.addItemDecoration(SpacesItemDecoration((requireContext().resources.displayMetrics.density * 16 + 0.5f).toInt(), true, 1))
    }

    override fun setUpOnClickListeners() {
        binding.checkout.setOnClickListener {
           navigate(R.id.action_nav_cart_to_nav_checkOut)
        }
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        data as CartResponse
        adapter.updateList(data.products as ArrayList<Product>)
        adapter.updateItems(viewModel.cart.value!!)
        binding.totalPrice.text=getString(R.string.money_format_sum,data.total)
    }

    override fun onRetry() {
        super.onRetry()

        mainActivity.showBottomNavigation()
        mainActivity.showToolbar()
        viewModel.getCart(productsList)
    }
    override fun setUpObservers() {

        viewModel.cart.observe(
            viewLifecycleOwner, Observer { products->
                products?.let {
                    productsList.clear()
                    for ( i in it.indices)
                    {
                        productsList["products[$i][id]"]=it[i].id
                        productsList["products[$i][count]"]=it[i].count
                    }
                    viewModel.getCart(productsList)
                }
            }
        )
        observeEvent(viewModel.cartResponse,::handle)
    }

}