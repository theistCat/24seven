package uz.usoft.a24seven.ui.profile.myOrders.orderLists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_my_inactive_orders.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.ui.profile.myOrders.MyOrderListRecyclerAdapter
import uz.usoft.a24seven.utils.SpacesItemDecoration

class MyInactiveOrdersListFragment : Fragment() {

    private lateinit var myOrderListRecyclerAdapter: MyOrderListRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_inactive_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myOrderListRecyclerAdapter= MyOrderListRecyclerAdapter()

        inactiveOrdersRecycler.layoutManager=LinearLayoutManager(requireContext())
        inactiveOrdersRecycler.adapter=myOrderListRecyclerAdapter
        inactiveOrdersRecycler.addItemDecoration(SpacesItemDecoration((resources.displayMetrics.density*16+0.5f).toInt(),true,1))

        myOrderListRecyclerAdapter.onItemClick={
            findNavController().navigate(R.id.action_nav_myOrders_to_nav_selectedOrder)
        }
    }
}