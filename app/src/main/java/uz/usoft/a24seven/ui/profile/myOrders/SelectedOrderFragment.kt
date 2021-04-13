package uz.usoft.a24seven.ui.profile.myOrders

import android.os.Bundle
import uz.usoft.a24seven.R
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.databinding.FragmentSelectedOrderBinding
import uz.usoft.a24seven.network.models.Order
import uz.usoft.a24seven.network.models.OrderItem
import uz.usoft.a24seven.ui.news.SelectedNewsFragmentArgs
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.observeEvent

class SelectedOrderFragment : BaseFragment<FragmentSelectedOrderBinding>(FragmentSelectedOrderBinding::inflate) {
    private lateinit var adapter: OrderItemListAdapter

    private val viewModel: OrdersViewModel by viewModel()
    private val safeArgs : SelectedOrderFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
        viewModel.showOrder(safeArgs.orderId)
    }

    override fun setUpUI() {
        super.setUpUI()

        mainActivity.hideBottomNavigation()
    }
    private fun setUpAdapter() {
        adapter = OrderItemListAdapter(requireContext())
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        data as Order
        adapter.updateList(data.products as ArrayList<OrderItem>)

        binding.orderDate.text=data.created_at
        binding.orderItemCount.text=data.products_count.toString()
        binding.orderAddress.text=data.address?.address
        binding.orderID.text=getString(R.string.order_number,data.id)
        binding.orderStatus.text=data.status
        binding.orderPaymentMethod.text=data.payment_type
        binding.orderTotalPrice.text=getString(R.string.money_format_sum,data.price_products+data.price_delivery)



    }

    override fun setUpObservers() {
        observeEvent(viewModel.showOrder,::handle)
    }

    override fun setUpRecyclers() {
        binding.orderItemList.adapter = adapter
        binding.orderItemList.layoutManager = LinearLayoutManager(requireContext())
        binding.orderItemList.addItemDecoration(
            SpacesItemDecoration(
                (resources.displayMetrics.density * 16 + 0.5f).toInt(),
                true,
                1
            )
        )
    }


}