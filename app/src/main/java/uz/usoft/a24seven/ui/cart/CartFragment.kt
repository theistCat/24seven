package uz.usoft.a24seven.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentCartBinding
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.navigate


class CartFragment : BaseFragment<FragmentCartBinding>(FragmentCartBinding::inflate) {

    private lateinit var adapter: CartItemListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
    }

    private fun setUpAdapter() {
        adapter = CartItemListAdapter()
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

}