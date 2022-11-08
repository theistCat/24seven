package a24seven.uz.ui.products

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import a24seven.uz.MainActivity
import a24seven.uz.R
import a24seven.uz.data.PrefManager
import a24seven.uz.databinding.FragmentNewProductsBinding
import a24seven.uz.databinding.SortBottomsheetBinding
import a24seven.uz.network.models.CartItem
import a24seven.uz.ui.home.ProductsListAdapter
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.SpacesItemDecoration
import a24seven.uz.utils.navigate
import a24seven.uz.utils.observeEvent
import a24seven.uz.utils.toDp
import a24seven.uz.utils.*

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
                selectedCategoryName = safeArgs.subCategoryName,
                productId = it.id
            )
            navigate(action)
        }
    }


    override fun setUpUI() {
            mainActivity.setTitle(safeArgs.subCategoryName)
            _bottomSheetBinding= SortBottomsheetBinding.inflate(layoutInflater)
            sortBottomSheet =
                createBottomSheet(bottomSheetBinding.root)

    }

    private fun onSort(option: View)
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
            updatePosition = product.id
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

