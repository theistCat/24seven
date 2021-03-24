package uz.usoft.a24seven.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentHomeBinding
import uz.usoft.a24seven.network.models.Compilation
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.network.utils.BaseFragment
import uz.usoft.a24seven.network.utils.NoConnectionDialogListener
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.ui.news.NewsListAdapter
import uz.usoft.a24seven.ui.noConnection.NoConnectionFragment
import uz.usoft.a24seven.utils.*

class HomeFragment : BaseFragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var imgList = ArrayList<String>()
    private lateinit var pagerAdapter: ImageCollectionAdapter
    val homeViewModel: HomeViewModel by viewModel()
    private lateinit var newProductsAdapter: ProductsListAdapter
    private lateinit var popularProductsAdapter: ProductsListAdapter
    private lateinit var onSaleProductsAdapter: ProductsListAdapter
    private lateinit var newsAdapter: NewsListAdapter
    private var recyclers: List<Compilation>?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpAdapter()
        homeViewModel.getHome()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return superOnCreateView(binding)
    }

    override fun setUpObservers() {
        homeViewModel.getHomeResponse.observe(viewLifecycleOwner,  {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        hideNoConnectionDialog()
                        showLoadingDialog()
                    }
                    is Resource.Success -> {
                        recyclers = resource.data.compilations
                        unhideRecyclers()
                        newsAdapter.updateList(resource.data.posts)
                        hideNoConnectionDialog()
                        hideLoadingDialog()
                    }
                    is Resource.GenericError -> {
                        hideLoadingDialog()
                        showSnackbar(resource.errorResponse.jsonResponse.getString("error"))
                    }
                    is Resource.Error -> {
                        hideLoadingDialog()
                        if (resource.exception is NoConnectivityException) {
                            showNoConnectionDialog()
                        }
                        //resource.exception.message?.let { it1 -> showSnackbar(it1) }
                    }
                }
            }
        })
    }


    private fun showRecycler(recycler:RecyclerView,adapter: ProductsListAdapter,list:ArrayList<Product>,title:TextView,showAll:TextView)
    {
        adapter.updateList(list)
        title.isVisible=true
        recycler.isVisible=true
        showAll.isVisible=true
    }

    override fun setUpPagers(){

        imgList.clear()
        imgList.add("https://i.imgur.com/0Q.png")
        imgList.add("https://i.imgur.com/0Q.png")
        imgList.add("https://i.imgur.com/0Q.png")
        pagerAdapter = ImageCollectionAdapter(this)
        pagerAdapter.updateImageList(imgList)
        setUpViewPager(pagerAdapter, binding.homePager, binding.homeTabLayout)
    }

    override fun setUpData() {
        //TODO("Not yet implemented")
    }

    private fun setUpAdapter() {
        newProductsAdapter = ProductsListAdapter(requireContext())
        popularProductsAdapter = ProductsListAdapter( requireContext())
        onSaleProductsAdapter = ProductsListAdapter(requireContext())


        popularProductsAdapter.onItemClick = {
            val action =
                HomeFragmentDirections.actionNavHomeToNavSelectedProduct(resources.getString(R.string.popular_items),it.id)
            navigate(action)
        }


        onSaleProductsAdapter.onItemClick = {
            val action =
                HomeFragmentDirections.actionNavHomeToNavSelectedProduct(resources.getString(R.string.on_sale_items),it.id)
            navigate(action)
        }

        newProductsAdapter.onItemClick = {
            val action =
                HomeFragmentDirections.actionNavHomeToNavSelectedProduct(resources.getString(R.string.title_newProducts),it.id)
            navigate(action)
        }

        newsAdapter = NewsListAdapter(requireContext())

        newsAdapter.onItemClick = {

            val action=HomeFragmentDirections.actionNavHomeToSelectedNewsFragment(it.id)
            navigate(action)
        }

    }

    override fun setUpRecyclers() {
        initProductRecyclers(binding.newItemsRecycler,newProductsAdapter)
        initProductRecyclers(binding.popularItemsRecycler,popularProductsAdapter)
        initProductRecyclers(binding.onSaleItemsRecycler,onSaleProductsAdapter)

        binding.newsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.newsRecycler.adapter = newsAdapter
        binding.newsRecycler.addItemDecoration(SpacesItemDecoration(toDp(16), false))

    }

    private fun initProductRecyclers(recycler: RecyclerView, adapter: ProductsListAdapter)
    {
        recycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter
        recycler.addItemDecoration(SpacesItemDecoration(toDp(16), false))
    }



    override fun setUpOnClickListeners() {
        binding.newItems.setOnClickListener {
           // findNavController().navigate(R.id.action_nav_home_to_nav_newProducts)
        }
        binding.newItemsAll.setOnClickListener {
        //    findNavController().navigate(R.id.action_nav_home_to_nav_newProducts)
        }

        binding.news.setOnClickListener {
            navigate(R.id.action_nav_home_to_newsFragment)
        }

        binding.allNews.setOnClickListener {
            navigate(R.id.action_nav_home_to_newsFragment)
        }

        //        scanBarCode.setOnClickListener {
//            findNavController().navigate(R.id.action_nav_home_to_nav_barcodeScanner)
//        }
//
//        favItems.setOnClickListener {
//            findNavController().navigate(R.id.action_nav_home_to_nav_myFavouriteItems)
//        }


    }

    override fun onResume() {
        super.onResume()
        unhideRecyclers()
    }

    private fun unhideRecyclers()
    {
        recyclers?.forEach { it->
            when(it.title)
            {
                "Новые товары"->
                    showRecycler(binding.newItemsRecycler,newProductsAdapter,it.products as ArrayList<Product>,binding.newItems,binding.newItemsAll)
                "Популярные товары"->
                    showRecycler(binding.popularItemsRecycler,popularProductsAdapter,it.products as ArrayList<Product>,binding.popularItems,binding.allPopularItems)
                "Скидки"->
                    showRecycler(binding.onSaleItemsRecycler,onSaleProductsAdapter,it.products as ArrayList<Product>,binding.onSaleItems,binding.allOnSaleItems)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)

    }

    override fun onRetryClicked() {
        homeViewModel.getHome()
        mainActivity.showBottomNavigation()
        mainActivity.showToolbar()

    }
}