package uz.usoft.a24seven.ui.profile.myOrders

import android.os.Bundle
import uz.usoft.a24seven.R
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.databinding.FragmentSelectedOrderBinding
import uz.usoft.a24seven.network.models.Order
import uz.usoft.a24seven.network.models.OrderItem
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.network.utils.Variables
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

    override fun setUpOnClickListeners() {
        super.setUpOnClickListeners()

        binding.cencelOrder.setOnClickListener {
            viewModel.cancelOrder(safeArgs.orderId)
        }
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
        binding.orderStatus.text=when (data.status) {
            Variables.orderType[1]!! -> {
                requireContext().getString(R.string.active)
            }

            Variables.orderType[0]!! -> {
                requireContext().getString(R.string.in_wait)
            }

            Variables.orderType[2]!! -> {
                requireContext().getString(R.string.delivered)
            }
            else -> {
                requireContext().getString(R.string.in_wait)
            }
        }
        binding.orderPaymentMethod.text=data.payment_type
        binding.orderTotalPrice.text=getString(R.string.money_format_sum,data.price_products+data.price_delivery)


        binding.cencelOrder.isVisible=data.status!=Variables.orderType[2]


    }

    override fun setUpObservers() {
        observeEvent(viewModel.showOrder,::handle)
        observeEvent(viewModel.cancelOrder,::handleCancelOrder)


    }

    private fun handleCancelOrder(event: Event<Resource<Any>>){
        event.getContentIfNotHandled()?.let { resource ->
            when (resource) {
                is Resource.Loading -> {
                    onLoading()
                }
                is Resource.Success -> {
                    findNavController().popBackStack()
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