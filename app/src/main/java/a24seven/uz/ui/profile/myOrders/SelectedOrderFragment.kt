package a24seven.uz.ui.profile.myOrders

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import a24seven.uz.R
import a24seven.uz.databinding.FragmentSelectedOrderBinding
import a24seven.uz.network.models.Order
import a24seven.uz.network.models.OrderItem
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.Resource
import a24seven.uz.network.utils.Variables
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.SpacesItemDecoration
import a24seven.uz.utils.observeEvent

class SelectedOrderFragment :
    BaseFragment<FragmentSelectedOrderBinding>(FragmentSelectedOrderBinding::inflate) {
    private lateinit var adapter: OrderItemListAdapter

    private val viewModel: OrdersViewModel by viewModel()
    private val safeArgs: SelectedOrderFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
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

        binding.orderDate.text = data.created_at
        binding.orderItemCount.text = data.products_count.toString()
        binding.orderAddress.text = data.address?.address
        binding.orderID.text = getString(R.string.order_number, data.id)
        binding.orderStatus.text = when (data.status) {
            Variables.orderType[1]!! -> {
                requireContext().getString(R.string.active)
            }

            Variables.orderType[0]!! -> {
                requireContext().getString(R.string.in_wait)
            }

            Variables.orderType[2]!! -> {
                requireContext().getString(R.string.delivered)
            }

            Variables.orderType[3]!! -> {
                getString(R.string.canceled)
            }

            else -> {
                data.status
            }
        }
        binding.orderPaymentMethod.text = data.payment_type
        binding.orderTotalPrice.text =
            getString(R.string.money_format_sum, data.price_products + data.price_delivery)

//        viewModel.setPriceList((data.price_delivery + data.price_products).toInt())
//        viewModel.addOne((data.price_delivery + data.price_products).toInt())

        binding.cencelOrder.isVisible = data.status != Variables.orderType[4]

        binding.cencelOrder.isEnabled = data.status != Variables.orderType[3]
    }

    override fun setUpObservers() {
        observeEvent(viewModel.showOrder, ::handle)
        observeEvent(viewModel.cancelOrder, ::handleCancelOrder)


    }

    private fun handleCancelOrder(event: Event<Resource<Any>>) {
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
                (resources.displayMetrics.density * 16 + 0.5f).toInt(), true, 1
            )
        )

    }


}