package uz.usoft.a24seven.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentHomeBinding
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.ui.news.NewsListAdapter
import uz.usoft.a24seven.utils.ImageCollectionAdapter
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.setUpViewPager
import uz.usoft.a24seven.utils.toDpi

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var imgList = ArrayList<String>()
    private lateinit var pagerAdapter: ImageCollectionAdapter
    val homeViewModel: HomeViewModel by viewModel()
    private lateinit var newProductsAdapter: ProductsListAdapter
    private lateinit var popularProductsAdapter: ProductsListAdapter
    private lateinit var onSaleProductsAdapter: ProductsListAdapter
    private lateinit var newsAdapter: NewsListAdapter


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
        setUpRecycler()
        setOnClickListener()
        setUpPager()
        setUpObservers()
        return binding.root
    }

    private fun setUpObservers() {
        homeViewModel.getHomeResponse.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        resource.data.compilations.forEach { it->
                            when(it.title)
                            {
                                "Новые товары"->
                                    newProductsAdapter.updateList(it.products)
                                "Популярные товары"->
                                    popularProductsAdapter.updateList(it.products)
                                "Скидки"->
                                    onSaleProductsAdapter.updateList(it.products)
                            }
                        }
                        newsAdapter.updateList(resource.data.posts)

                    }
                    is Resource.GenericError -> {
                    }
                    is Resource.Error -> {
                    }
                }
            }
        })
    }

    private fun setUpPager(){

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

        popularProductsAdapter.onItemClick = {
            val action =
                HomeFragmentDirections.actionNavHomeToNavSelectedProduct(resources.getString(R.string.popular_items))
            findNavController().navigate(action)
        }

        onSaleProductsAdapter = ProductsListAdapter(requireContext())

        onSaleProductsAdapter.onItemClick = {
            val action =
                HomeFragmentDirections.actionNavHomeToNavSelectedProduct(resources.getString(R.string.on_sale_items))
            findNavController().navigate(action)
        }

        newProductsAdapter.onItemClick = {
            val action =
                HomeFragmentDirections.actionNavHomeToNavSelectedProduct(resources.getString(R.string.title_newProducts))
            findNavController().navigate(action)
        }

        newsAdapter = NewsListAdapter(requireContext())

        newsAdapter.onItemClick = {
            findNavController().navigate(R.id.action_nav_home_to_selectedNewsFragment)
        }

    }

    private fun setUpRecycler() {
        binding.newItemsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.newItemsRecycler.adapter = newProductsAdapter
        binding.newItemsRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16), false))


        binding.popularItemsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.popularItemsRecycler.adapter = popularProductsAdapter
        binding.popularItemsRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16), false))

        binding.onSaleItemsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.onSaleItemsRecycler.adapter = onSaleProductsAdapter
        binding.onSaleItemsRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16), false))

        binding.newsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.newsRecycler.adapter = newsAdapter
        binding.newsRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16), false))

    }


    private fun setOnClickListener() {
        binding.newItems.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_newProducts)
        }
        binding.newItemsAll.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_newProducts)
        }

        binding.news.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_newsFragment)
        }

        binding.allNews.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_newsFragment)
        }

        //        scanBarCode.setOnClickListener {
//            findNavController().navigate(R.id.action_nav_home_to_nav_barcodeScanner)
//        }
//
//        favItems.setOnClickListener {
//            findNavController().navigate(R.id.action_nav_home_to_nav_myFavouriteItems)
//        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)

    }
}