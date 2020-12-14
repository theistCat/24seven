package uz.usoft.a24seven.ui.profile.myOrders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_selected_order.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.utils.SpacesItemDecoration

class SelectedOrderFragment : Fragment() {

    private lateinit var adapter: OrderItemListAdapter
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
        return inflater.inflate(R.layout.fragment_selected_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= OrderItemListAdapter()
        orderItemList.adapter=adapter
        orderItemList.layoutManager=LinearLayoutManager(requireContext())
        orderItemList.addItemDecoration(SpacesItemDecoration((resources.displayMetrics.density*16+0.5f).toInt(),true,1))
    }
}