package uz.usoft.a24seven.ui.profile.myOrders.orderLists

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_my_active_orders_list.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentMyActiveOrdersListBinding
import uz.usoft.a24seven.databinding.FragmentMyFavouriteItemsBinding
import uz.usoft.a24seven.ui.profile.myOrders.MyOrderListRecyclerAdapter
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.toDpi


class MyActiveOrdersListFragment : Fragment() {
    private lateinit var myOrderListRecyclerAdapter: MyOrderListRecyclerAdapter
    private var _binding: FragmentMyActiveOrdersListBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentMyActiveOrdersListBinding.inflate(inflater, container, false)
        setUpAdapters()
        setUpClickListener()
        return binding.root
    }

    private fun setUpAdapters() {
        myOrderListRecyclerAdapter = MyOrderListRecyclerAdapter("active")
        myOrderListRecyclerAdapter.onItemClick = {
            findNavController().navigate(R.id.action_nav_myOrders_to_nav_selectedOrder)
        }
    }

    private fun setUpClickListener() {
        binding.activeOrdersRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.activeOrdersRecycler.adapter = myOrderListRecyclerAdapter
        binding.activeOrdersRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16), true, 1))

    }
}