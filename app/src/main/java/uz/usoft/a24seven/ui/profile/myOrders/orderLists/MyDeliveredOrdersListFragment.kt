package uz.usoft.a24seven.ui.profile.myOrders.orderLists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_my_delivered_orders.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentMyActiveOrdersListBinding
import uz.usoft.a24seven.databinding.FragmentMyDeliveredOrdersBinding
import uz.usoft.a24seven.ui.profile.myOrders.MyOrderListRecyclerAdapter
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.toDpi


class MyDeliveredOrdersListFragment : Fragment() {

    private lateinit var myOrderListRecyclerAdapter: MyOrderListRecyclerAdapter
    private var _binding: FragmentMyDeliveredOrdersBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapters()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyDeliveredOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setUpAdapters() {
        myOrderListRecyclerAdapter = MyOrderListRecyclerAdapter("delivered")
        myOrderListRecyclerAdapter.onItemClick = {
            findNavController().navigate(R.id.action_nav_myOrders_to_nav_selectedOrder)
        }
    }

    private fun setUpClickListener() {
        binding.deliveredOrdersRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.deliveredOrdersRecycler.adapter = myOrderListRecyclerAdapter
        binding.deliveredOrdersRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16), true, 1))

    }


}