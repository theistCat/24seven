package uz.usoft.a24seven.ui.home

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentHomeBinding
import uz.usoft.a24seven.network.models.Compilation
import uz.usoft.a24seven.network.models.HomeResponse
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.ui.news.NewsListAdapter
import uz.usoft.a24seven.utils.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate){

    private var imgList = ArrayList<String>()
    private lateinit var pagerAdapter: ImageCollectionAdapter

    private val homeViewModel: HomeViewModel by viewModel()

    private lateinit var newProductsAdapter: ProductsListAdapter
    private lateinit var popularProductsAdapter: ProductsListAdapter
    private lateinit var onSaleProductsAdapter: ProductsListAdapter
    private lateinit var newsAdapter: NewsListAdapter
    private var recyclers: List<Compilation>?=null

    private var updateId=-1
    private var updateValue=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpAdapter()
        homeViewModel.getHome()

    }

    override fun setUpUI() {
        binding.swipeToRefresh.setOnRefreshListener {
            homeViewModel.getHome()
        }
    }

    override fun getData() {
        homeViewModel.getHome()
    }

    override fun setUpObservers() {
        observeEvent(homeViewModel.getHomeResponse, ::handle)
        observeEvent(homeViewModel.favResponse,::handle)
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        when(data) {
            is HomeResponse -> {
                recyclers = data.compilations
                unhideRecyclers()
                newsAdapter.updateList(data.posts)
                binding.swipeToRefresh.isRefreshing = false
            }
            else ->{
                    updateProductList()
            }
        }
    }

    private fun updateProductList() {
        recyclers?.forEach { it->
            when(it.title)
            {
                getString(R.string.new_items)->
                    newProductsAdapter.update(updateId,updateValue)
                getString(R.string.popular_items)->
                    popularProductsAdapter.update(updateId,updateValue)
                getString(R.string.on_sale_items)->
                    onSaleProductsAdapter.update(updateId,updateValue)
            }
        }
    }

    override fun onRetry() {
        homeViewModel.getHome()
        mainActivity.showBottomNavigation()
        mainActivity.showToolbar()
    }

    override fun onError(resource: Resource.Error) {
        super.onError(resource)
        binding.swipeToRefresh.isRefreshing=false
    }

    override fun onGenericError(resource: Resource.GenericError) {
        super.onGenericError(resource)
        binding.swipeToRefresh.isRefreshing=false
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

    private fun setUpAdapter() {
        newProductsAdapter = ProductsListAdapter(requireContext())
        popularProductsAdapter = ProductsListAdapter( requireContext())
        onSaleProductsAdapter = ProductsListAdapter(requireContext())

        val adapterList= ArrayList<ProductsListAdapter>()
        adapterList.add(newProductsAdapter)
        adapterList.add(popularProductsAdapter)
        adapterList.add(onSaleProductsAdapter)

        adapterList.forEach {
            it.addFav = {product->
                updateId=product.id
                updateValue=true
                homeViewModel.addFav(product.id)
            }

            it.removeFav = {product->
                updateId=product.id
                updateValue=false
                homeViewModel.removeFav(product.id)
            }
        }

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
                getString(R.string.new_items)->
                    showRecycler(binding.newItemsRecycler,newProductsAdapter,it.products as ArrayList<Product>,binding.newItems,binding.newItemsAll)
                getString(R.string.popular_items)->
                    showRecycler(binding.popularItemsRecycler,popularProductsAdapter,it.products as ArrayList<Product>,binding.popularItems,binding.allPopularItems)
                getString(R.string.on_sale_items)->
                    showRecycler(binding.onSaleItemsRecycler,onSaleProductsAdapter,it.products as ArrayList<Product>,binding.onSaleItems,binding.allOnSaleItems)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)

    }

}