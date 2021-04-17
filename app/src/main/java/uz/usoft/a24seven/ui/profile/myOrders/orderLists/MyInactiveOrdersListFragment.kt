package uz.usoft.a24seven.ui.profile.myOrders.orderLists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentMyInactiveOrdersBinding
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Variables
import uz.usoft.a24seven.ui.profile.myOrders.MyOrderListRecyclerAdapter
import uz.usoft.a24seven.ui.profile.myOrders.OrdersViewModel
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.toDp

class MyInactiveOrdersListFragment : BaseFragment<FragmentMyInactiveOrdersBinding>(FragmentMyInactiveOrdersBinding::inflate) {

    private val viewModel: OrdersViewModel by viewModel()
    private lateinit var myOrderListRecyclerAdapter: MyOrderListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapters()
        getInWaitOrders()
    }
    private fun getInWaitOrders() {
        lifecycleScope.launch {
            viewModel.getOrders(Variables.orderType[0]!!).collect {
                myOrderListRecyclerAdapter.submitData(it)
                return@collect
            }
        }
    }

    private fun setUpAdapters() {
        myOrderListRecyclerAdapter = MyOrderListRecyclerAdapter(requireContext(),Variables.orderType[0]!!)
        myOrderListRecyclerAdapter.onItemClick = {
            findNavController().navigate(R.id.action_nav_myOrders_to_nav_selectedOrder)
        }
    }

    override fun setUpRecyclers() {

        binding.inactiveOrdersRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.inactiveOrdersRecycler.adapter = myOrderListRecyclerAdapter
        binding.inactiveOrdersRecycler.addItemDecoration(SpacesItemDecoration(toDp(16), true, 1))

    }


    override fun setUpObservers() {

        lifecycleScope.launch {
            myOrderListRecyclerAdapter.loadStateFlow.collectLatest { loadStates ->
                //progressBar.isVisible = loadStates.refresh is LoadState.Loading
                //retry.isVisible = loadState.refresh !is LoadState.Loading
                when(loadStates.refresh)
                {
                    is LoadState.Error->{
                        val error = loadStates.refresh as LoadState.Error
                        if (error.error is NoConnectivityException)
                        {
                            showNoConnectionDialog(this@MyInactiveOrdersListFragment::onRetry)
                        }
                    }
                    is LoadState.Loading->{
                        hideNoConnectionDialog()
                        showLoadingDialog()
                    }
                    else->{
                        hideNoConnectionDialog()
                        hideLoadingDialog()
                        if(myOrderListRecyclerAdapter.itemCount<1)
                        {
                            binding.noOrders.visibility= View.VISIBLE
                        }
                    }
                }

            }
        }
    }
}