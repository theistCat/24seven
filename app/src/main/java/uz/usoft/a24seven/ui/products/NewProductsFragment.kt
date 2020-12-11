package uz.usoft.a24seven.ui.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_new_products.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.utils.SpacesItemDecoration

class NewProductsFragment : Fragment() {

    private lateinit var adapter: ProductsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= ProductsListAdapter(true)
        newProductsRecycler.adapter=adapter
        newProductsRecycler.layoutManager=GridLayoutManager(requireContext(),2)
        val density=requireContext().resources.displayMetrics.density
        newProductsRecycler.addItemDecoration(SpacesItemDecoration((16*density+0.5f).toInt()))



    }
}