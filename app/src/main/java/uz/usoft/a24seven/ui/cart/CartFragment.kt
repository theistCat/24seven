package uz.usoft.a24seven.ui.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_cart.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.utils.SpacesItemDecoration


class CartFragment : Fragment() {

    private lateinit var adapter:CartItemListAdapter
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
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= CartItemListAdapter()
        cartRecycler.adapter=adapter
        cartRecycler.layoutManager=LinearLayoutManager(requireContext())
        cartRecycler.addItemDecoration(SpacesItemDecoration((requireContext().resources.displayMetrics.density*16+0.5f).toInt(),true,1))

    }
}