package uz.usoft.a24seven.ui.profile.myFavouriteItems

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentMyFavouriteItemsBinding
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.createBottomSheet
import uz.usoft.a24seven.utils.toDp

class MyFavouriteItemsFragment : Fragment() {

    private var _binding: FragmentMyFavouriteItemsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ProductsListAdapter
    private lateinit var  sortBottomSheet: BottomSheetDialog

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
        _binding = FragmentMyFavouriteItemsBinding.inflate(inflater, container, false)
        sortBottomSheet= createBottomSheet(R.layout.sort_bottomsheet)
        setUpOnRecycler()
        setUpOnClickerListener()
        return binding.root
    }

    private fun setUpAdapters() {
        adapter = ProductsListAdapter(requireContext(),isGrid = true)
        adapter.onItemClick = {
//            val action =
//                MyFavouriteItemsFragmentDirections.actionNavMyFavouriteItemsToNavSelectedProduct(
//                    resources.getString(R.string.title_myFavourites)
//                )
//            findNavController().navigate(action)
        }
    }

    private fun setUpOnRecycler() {
        binding.favouriteProductsRecycler.adapter = adapter
        binding.favouriteProductsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.favouriteProductsRecycler.addItemDecoration(SpacesItemDecoration(toDp(16)))
    }

    private fun setUpOnClickerListener() {

        binding.filter.setOnClickListener {
            (requireActivity() as MainActivity).openDrawer()
        }


        binding.sortBy.setOnClickListener {
            sortBottomSheet.show()
        }

    }


}