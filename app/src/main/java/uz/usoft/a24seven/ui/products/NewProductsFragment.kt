package uz.usoft.a24seven.ui.products

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.databinding.FragmentNewProductsBinding
import uz.usoft.a24seven.databinding.SortBottomsheetBinding
import uz.usoft.a24seven.network.models.CartItem
import uz.usoft.a24seven.network.utils.Variables
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.*

class NewProductsFragment : BaseFragment<FragmentNewProductsBinding>(FragmentNewProductsBinding::inflate) {

    private lateinit var adapter: ProductsListAdapter
    private lateinit var  sortBottomSheet:BottomSheetDialog
    private var  _bottomSheetBinding:SortBottomsheetBinding?=null
    private val  bottomSheetBinding get() = _bottomSheetBinding!!
    private val safeArgs: NewProductsFragmentArgs by navArgs()
    private val productViewModel: ProductViewModel by viewModel()

    private var updatePosition:Int=-1
    private var updateValue:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
    }


    private fun setUpAdapter() {
        adapter = ProductsListAdapter(requireContext(),isGrid = true)
        adapter.updateList(safeArgs.products.list)
        adapter.onItemClick = {
            val action = NewProductsFragmentDirections.actionNavNewProductsToNavSelectedProduct(
                safeArgs.subCategoryName,
                it.id
            )
            navigate(action)
        }
    }


    override fun setUpUI() {
            mainActivity.setTitle(safeArgs.subCategoryName)
            _bottomSheetBinding= SortBottomsheetBinding.inflate(layoutInflater)
            sortBottomSheet = createBottomSheet(bottomSheetBinding.root)

    }

    fun onSort(option: View)
    {
        when(option.id)
        {
            bottomSheetBinding.sortByNew.id->{
                binding.sortBy.text=getString(R.string.sort_by_new)
            }
            bottomSheetBinding.sortByPopular.id->{
                binding.sortBy.text=getString(R.string.sort_by_popular)
            }
            bottomSheetBinding.sortByCheap.id->{
                binding.sortBy.text=getString(R.string.sort_by_cheap)

            }
            bottomSheetBinding.sortByExpensive.id->{
                binding.sortBy.text=getString(R.string.sort_by_expensive)

            }
        }
        sortBottomSheet.dismiss()
    }

    override fun setUpRecyclers() {
        binding.newProductsRecycler.adapter = adapter
        binding.newProductsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.newProductsRecycler.addItemDecoration(SpacesItemDecoration(toDp(16)))

    }

    override fun setUpOnClickListeners() {

        adapter.onFavClick = {product,position->
            updatePosition = position
            updateValue = !product.is_favorite

            Log.d("favTag", "change ${product.id} at position $position")

            if(!product.is_favorite) {

                Log.d("favTag", "add")
                productViewModel.addFav(product.id)
            }
            else{

                Log.d("favTag", "remove")
                productViewModel.removeFav(product.id)
            }
        }

        adapter.addToCart={
            productViewModel.addToCart(CartItem(it.id,1))
        }

        binding.filter.setOnClickListener {
            (requireActivity() as MainActivity).openDrawer()
        }
        binding.sortBy.setOnClickListener {
            sortBottomSheet.show()
        }

        bottomSheetBinding.sortByNew.setOnClickListener {
            onSort(it)
        }
        bottomSheetBinding.sortByPopular.setOnClickListener {
            onSort(it)
        }
        bottomSheetBinding.sortByCheap.setOnClickListener {
            onSort(it)
        }
        bottomSheetBinding.sortByExpensive.setOnClickListener {
            onSort(it)
        }
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)


        Log.d("favTag", "success")
        adapter.update(updatePosition,updateValue)
    }

    override fun setUpObservers() {
        super.setUpObservers()
        observeEvent(productViewModel.favResponse,::handle)

        productViewModel.addToCartResponse.observe(
            viewLifecycleOwner, Observer { result->
                result?.let {
                    if(it.toInt()!=-1)
                    {
                        PrefManager.getInstance(requireContext()).edit().putBoolean(it.toString(),true).apply()
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        )
    }
}

