package uz.usoft.a24seven.ui.profile.myFavouriteItems

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_my_favourite_items.*
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.createBottomSheet
import uz.usoft.a24seven.utils.toDpi

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
        favouriteProductsRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16)))

        adapter.onItemClick={
            val action= MyFavouriteItemsFragmentDirections.actionNavMyFavouriteItemsToNavSelectedProduct(resources.getString(R.string.title_myFavourites))
            findNavController().navigate(action)
        }

        filter.setOnClickListener {
            (requireActivity() as MainActivity).openDrawer()
        }

        val sortBottomSheet=
                createBottomSheet(R.layout.sort_bottomsheet)
        sortBy.setOnClickListener {
            sortBottomSheet.show()
        }
    }
}