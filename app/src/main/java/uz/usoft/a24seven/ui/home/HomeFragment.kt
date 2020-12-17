package uz.usoft.a24seven.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.ui.news.NewsListAdapter
import uz.usoft.a24seven.utils.ImageCollectionAdapter
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.setUpViewPager

class HomeFragment : Fragment() {

    private lateinit var pagerAdapter: ImageCollectionAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var newProductsAdapter:ProductsListAdapter
    private lateinit var popularProductsAdapter:ProductsListAdapter
    private lateinit var onSaleProductsAdapter:ProductsListAdapter
    private lateinit var newsAdapter: NewsListAdapter

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerAdapter= ImageCollectionAdapter(this)
        setUpViewPager(pagerAdapter,homePager,homeTabLayout)

        var imgList=ArrayList<String>()

        imgList.add("https://i.imgur.com/0Q.png")
        imgList.add("https://i.imgur.com/0Q.png")
        imgList.add("https://i.imgur.com/0Q.png")

        pagerAdapter.updateImageList(imgList)



//        scanBarCode.setOnClickListener {
//            findNavController().navigate(R.id.action_nav_home_to_nav_barcodeScanner)
//        }
//
//        favItems.setOnClickListener {
//            findNavController().navigate(R.id.action_nav_home_to_nav_myFavouriteItems)
//        }
        newProductsAdapter= ProductsListAdapter()
        newItemsRecycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        newItemsRecycler.adapter=newProductsAdapter
        newItemsRecycler.addItemDecoration(SpacesItemDecoration(16,false))

        newProductsAdapter.onItemClick={
            val action= HomeFragmentDirections.actionNavHomeToNavSelectedProduct(resources.getString(R.string.title_newProducts))
            findNavController().navigate(action)
        }

        newItems.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_newProducts)
        }
        newItemsAll.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_newProducts)
        }


        popularProductsAdapter= ProductsListAdapter(isPopular = true)
        popularItemsRecycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        popularItemsRecycler.adapter=popularProductsAdapter
        popularItemsRecycler.addItemDecoration(SpacesItemDecoration(16,false))

        popularProductsAdapter.onItemClick={
            val action= HomeFragmentDirections.actionNavHomeToNavSelectedProduct(resources.getString(R.string.popular_items))
            findNavController().navigate(action)
        }


        onSaleProductsAdapter= ProductsListAdapter()
        onSaleItemsRecycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        onSaleItemsRecycler.adapter=onSaleProductsAdapter
        onSaleItemsRecycler.addItemDecoration(SpacesItemDecoration(16,false))

        onSaleProductsAdapter.onItemClick={
            val action= HomeFragmentDirections.actionNavHomeToNavSelectedProduct(resources.getString(R.string.on_sale_items))
            findNavController().navigate(action)
        }

        newsAdapter= NewsListAdapter()
        newsRecycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        newsRecycler.adapter=newsAdapter
        newsRecycler.addItemDecoration(SpacesItemDecoration(16,false))

        news.setOnClickListener{
            findNavController().navigate(R.id.action_nav_home_to_newsFragment)
        }

        allNews.setOnClickListener{
            findNavController().navigate(R.id.action_nav_home_to_newsFragment)
        }

        newsAdapter.onItemClick={
                findNavController().navigate(R.id.action_nav_home_to_selectedNewsFragment)
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)

    }
}