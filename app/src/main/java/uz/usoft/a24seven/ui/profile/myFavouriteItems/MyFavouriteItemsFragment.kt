package uz.usoft.a24seven.ui.profile.myFavouriteItems

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_my_favourite_items.*
import kotlinx.android.synthetic.main.fragment_new_products.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.ui.products.NewProductsFragmentDirections
import uz.usoft.a24seven.utils.SpacesItemDecoration

class MyFavouriteItemsFragment : Fragment() {


    private lateinit var adapter: ProductsListAdapter
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
        return inflater.inflate(R.layout.fragment_my_favourite_items, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= ProductsListAdapter(true)
        favouriteProductsRecycler.adapter=adapter
        favouriteProductsRecycler.layoutManager= GridLayoutManager(requireContext(),2)
        val density=resources.displayMetrics.density
        favouriteProductsRecycler.addItemDecoration(SpacesItemDecoration((16*density+0.5f).toInt()))

        adapter.onItemClick={
            val action= MyFavouriteItemsFragmentDirections.actionNavMyFavouriteItemsToNavSelectedProduct(resources.getString(R.string.title_myFavourites))
            findNavController().navigate(action)
        }

    }
}