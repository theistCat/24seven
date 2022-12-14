package a24seven.uz.ui.profile.myOrders.orderLists

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import a24seven.uz.databinding.FragmentMyDeliveredOrdersBinding
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
import java.text.SimpleDateFormat


class MyDeliveredOrdersListFragment :
    BaseFragment<FragmentMyDeliveredOrdersBinding>(FragmentMyDeliveredOrdersBinding::inflate) {

    private val viewModel: OrdersViewModel by viewModel()
    private lateinit var myOrderListRecyclerAdapter: MyOrderListRecyclerAdapter

    val simpleDateFormat = SimpleDateFormat("hh:mm, dd.MM.yyyy")
    var dateFrom = 0L
    var dateTo = 0L

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
                            showNoConnectionDialog(this@MyDeliveredOrdersListFragment::onRetry)
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
                            binding.llSumFilter.visibility = View.GONE
                        } else binding.llSumFilter.visibility = View.VISIBLE
                    }
                }

            }
        }
    }

    private fun getDeliveredOrders() {
        lifecycleScope.launch {
            viewModel.getOrders(Variables.orderType[2]!!).collect {
                myOrderListRecyclerAdapter.submitData(it)
                return@collect
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        val countSum = "??????????: ${viewModel.number} ??????"
//        binding.sumCount.text = countSum
    }

    private fun filterClick() {
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())
                .build()
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setCalendarConstraints(constraintsBuilder)
                .build()

        dateRangePicker.show(requireFragmentManager(), "tag")
        dateRangePicker.addOnPositiveButtonClickListener {
//            binding.sumCount.text = (dateRangePicker.selection?.second?.div(60)?.div(60)
//                ?.div(24)!! - dateRangePicker.selection?.first?.div(60)?.div(60)?.div(24)!!).div(
//                1000
//            ).toString()
            dateFrom = dateRangePicker.selection?.first!!
            dateTo = dateRangePicker.selection?.second!!

//            viewModel.dateFrom = dateFrom!!
//            viewModel.dateTo = dateTo!!

            var cumm = 0

            for (o in 0 until viewModel.priceListLive2.count()) {
                val j = simpleDateFormat.parse(viewModel.dateListLive2[o])!!
                if (j.time in dateFrom..dateTo) {
                    cumm += viewModel.priceListLive2[o]
                }
            }


            val countSum = "??????????: $cumm ??????"
            binding.sumCount.text = countSum
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.priceListLive2.clear()
        viewModel.dateListLive2.clear()
    }

    override fun setUpObservers() {


    }

    private fun setUpAdapters() {
        myOrderListRecyclerAdapter =
            MyOrderListRecyclerAdapter(requireContext(), Variables.orderType[2]!!, viewModel)
        myOrderListRecyclerAdapter.onItemClick = {
            val action = MyOrdersFragmentDirections.actionNavMyOrdersToNavSelectedOrder(it.id)
            navigate(action)
        }
    }

    override fun setUpRecyclers() {
        binding.deliveredOrdersRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.deliveredOrdersRecycler.adapter = myOrderListRecyclerAdapter
        binding.deliveredOrdersRecycler.addItemDecoration(SpacesItemDecoration(toDp(16), true, 1))
        binding.filterBtn.setOnClickListener { filterClick() }
        binding.sumCount.text = "?????????????? ???????????? >"
    }


}