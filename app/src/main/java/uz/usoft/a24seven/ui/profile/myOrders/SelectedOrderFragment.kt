package uz.usoft.a24seven.ui.profile.myOrders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_selected_order.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentMyInactiveOrdersBinding
import uz.usoft.a24seven.databinding.FragmentSelectedOrderBinding
import uz.usoft.a24seven.utils.SpacesItemDecoration

class SelectedOrderFragment : Fragment() {

    private var _binding: FragmentSelectedOrderBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: OrderItemListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSelectedOrderBinding.inflate(inflater, container, false)
        setUpRecycler()
        return binding.root
    }


    private fun setUpAdapter() {
        adapter = OrderItemListAdapter()

    }

    private fun setUpRecycler() {
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