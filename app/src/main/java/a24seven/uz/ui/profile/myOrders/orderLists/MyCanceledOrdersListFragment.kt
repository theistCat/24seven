package a24seven.uz.ui.profile.myOrders.orderLists

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import a24seven.uz.databinding.FragmentMyCanceledOrdersBinding
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.network.utils.Variables
import a24seven.uz.ui.profile.myOrders.MyOrderListRecyclerAdapter
import a24seven.uz.ui.profile.myOrders.MyOrdersFragmentDirections
import a24seven.uz.ui.profile.myOrders.OrdersViewModel
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.SpacesItemDecoration
import a24seven.uz.utils.navigate
import a24seven.uz.utils.showSnackbar
import a24seven.uz.utils.toDp


class MyCanceledOrdersListFragment :
    BaseFragment<FragmentMyCanceledOrdersBinding>(FragmentMyCanceledOrdersBinding::inflate) {

    private val viewModel: OrdersViewModel by viewModel()
    private lateinit var myOrderListRecyclerAdapter: MyOrderListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapters()
        getDeliveredOrders()
        lifecycleScope.launch {
            myOrderListRecyclerAdapter.loadStateFlow.collectLatest { loadStates ->
                //progressBar.isVisible = loadStates.refresh is LoadState.Loading
                //retry.isVisible = loadState.refresh !is LoadState.Loading
                when (loadStates.refresh) {
                    is LoadState.Error -> {
                        val error = loadStates.refresh as LoadState.Error
                        hideLoadingDialog()
                        if (error.error is NoConnectivityException) {
                            showNoConnectionDialog(this@MyCanceledOrdersListFragment::onRetry)
                        } else {
                            showSnackbar(error.error.message.toString())
                        }
                    }
                    is LoadState.Loading -> {
                        hideNoConnectionDialog()
                        showLoadingDialog()
                    }
                    else -> {
                        hideNoConnectionDialog()
                        hideLoadingDialog()
                        if (myOrderListRecyclerAdapter.itemCount < 1) {
                            binding.noOrders.visibility = View.VISIBLE
                        }
                    }
                }

            }
        }
    }

    private fun getDeliveredOrders() {
        lifecycleScope.launch {
            viewModel.getOrders(Variables.orderType[3]!!).collect {
                myOrderListRecyclerAdapter.submitData(it)
                return@collect
            }
        }
    }


    override fun setUpObservers() {


    }

    private fun setUpAdapters() {
        myOrderListRecyclerAdapter =
            MyOrderListRecyclerAdapter(requireContext(), Variables.orderType[3]!!, viewModel)
        myOrderListRecyclerAdapter.onItemClick = {
            val action = MyOrdersFragmentDirections.actionNavMyOrdersToNavSelectedOrder(it.id)
            navigate(action)
        }
    }

    override fun setUpRecyclers() {
        binding.deliveredOrdersRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.deliveredOrdersRecycler.adapter = myOrderListRecyclerAdapter
        binding.deliveredOrdersRecycler.addItemDecoration(SpacesItemDecoration(toDp(16), true, 1))
    }


}