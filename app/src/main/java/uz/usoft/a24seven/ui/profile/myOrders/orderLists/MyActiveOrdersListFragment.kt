package uz.usoft.a24seven.ui.profile.myOrders.orderLists

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlinx.coroutines.launch
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentMyActiveOrdersListBinding
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Variables
import uz.usoft.a24seven.ui.profile.myOrders.MyOrderListRecyclerAdapter
import uz.usoft.a24seven.ui.profile.myOrders.MyOrdersFragmentDirections
import uz.usoft.a24seven.ui.profile.myOrders.OrdersViewModel
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.showSnackbar
import uz.usoft.a24seven.utils.toDp


class MyActiveOrdersListFragment : BaseFragment<FragmentMyActiveOrdersListBinding>(FragmentMyActiveOrdersListBinding::inflate) {

    private val viewModel: OrdersViewModel by viewModel()
    private lateinit var myOrderListRecyclerAdapter: MyOrderListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpAdapters()
        getActiveOrders()
        lifecycleScope.launch {
            myOrderListRecyclerAdapter.loadStateFlow.collectLatest { loadStates ->
                //progressBar.isVisible = loadStates.refresh is LoadState.Loading
                //retry.isVisible = loadState.refresh !is LoadState.Loading
                when(loadStates.refresh)
                {
                    is LoadState.Error->{
                        val error = loadStates.refresh as LoadState.Error
                        hideLoadingDialog()
                        if (error.error is NoConnectivityException)
                        {
                            showNoConnectionDialog(this@MyActiveOrdersListFragment::onRetry)
                        }else{
                            showSnackbar(error.error.message.toString())
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

    private fun getActiveOrders() {
        lifecycleScope.launch {
            viewModel.getOrders(Variables.orderType[1]!!).collect {
                myOrderListRecyclerAdapter.submitData(it)
                return@collect
            }
        }
    }

    override fun setUpObservers() {
//        lifecycleScope.launch {
//            myOrderListRecyclerAdapter.loadStateFlow.collectLatest { loadStates ->
//                //progressBar.isVisible = loadStates.refresh is LoadState.Loading
//                //retry.isVisible = loadState.refresh !is LoadState.Loading
//                when(loadStates.refresh)
//                {
//                    is LoadState.Error->{
//                        val error = loadStates.refresh as LoadState.Error
//                        if (error.error is NoConnectivityException)
//                        {
//                            showNoConnectionDialog(this@MyActiveOrdersListFragment::onRetry)
//                        }
//                    }
//                    is LoadState.Loading->{
//                        hideNoConnectionDialog()
//                        showLoadingDialog()
//                    }
//                    else->{
//                        hideNoConnectionDialog()
//                        hideLoadingDialog()
//                        if(myOrderListRecyclerAdapter.itemCount<1)
//                        {
//                            binding.noOrders.visibility= View.VISIBLE
//                        }
//                    }
//                }
//
//            }
//        }
    }

    private fun setUpAdapters() {
        myOrderListRecyclerAdapter = MyOrderListRecyclerAdapter(requireContext(),Variables.orderType[1]!!)
        myOrderListRecyclerAdapter.onItemClick = {
            val action=MyOrdersFragmentDirections.actionNavMyOrdersToNavSelectedOrder(it.id)
            navigate(action)
        }
    }

    override fun setUpOnClickListeners() {
        binding.activeOrdersRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.activeOrdersRecycler.adapter = myOrderListRecyclerAdapter
        binding.activeOrdersRecycler.addItemDecoration(SpacesItemDecoration(toDp(16), true, 1))


    }

}