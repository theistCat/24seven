package uz.usoft.a24seven

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import uz.usoft.a24seven.databinding.ActivityMainBinding
import uz.usoft.a24seven.ui.filter.FilterFragment
import uz.usoft.a24seven.utils.*
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.network.models.OrderItem
import uz.usoft.a24seven.network.models.SubCategoriesObject
import uz.usoft.a24seven.ui.auth.AuthActivity
import uz.usoft.a24seven.ui.category.CategoryFragmentDirections
import uz.usoft.a24seven.ui.category.selectedSubCategory.SelectedSubCategoryFragmentDirections
import uz.usoft.a24seven.ui.category.subCategory.SubCategoriesFragmentDirections
import uz.usoft.a24seven.ui.home.HomeFragmentDirections
import uz.usoft.a24seven.ui.seach.SearchActivity

class MainActivity : AppCompatActivity(), DrawerLayout.DrawerListener {

    companion object{

        lateinit var openAuthActivityCustom:ActivityResultLauncher<Intent>
        lateinit var openSearchActivityCustom:ActivityResultLauncher<Intent>
        const val ACCESS_TOKEN="access_token"
        const val SEARCH_RESULT="searched_product_id"

    }

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    var onSearchResult: ((Int) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_24seven)

//        PrefManager.saveToken(this,"")

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        openAuthActivityCustom =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    Activity.RESULT_OK -> {
                        Log.d(ACCESS_TOKEN, "access_token : ${result.data?.getStringExtra(ACCESS_TOKEN).toString()}")
                        PrefManager.saveToken(this, result.data?.getStringExtra(ACCESS_TOKEN).toString())
                        navController.popBackStack(R.id.nav_home,false)
                        navController.navigate(R.id.nav_profile)
                    }
                    Activity.RESULT_CANCELED->{
                        navController.popBackStack(R.id.nav_home,false)
                    }
                }
            }

        openSearchActivityCustom =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    Activity.RESULT_OK -> {
                        onSearchResult?.invoke(result.data?.getIntExtra(SEARCH_RESULT,0)?:0)
                    }
                    Activity.RESULT_CANCELED->{

                    }
                }
            }

        bottomNavigationView = binding.navView
        drawerLayout = binding.drawerFragment
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        drawerLayout.addDrawerListener(this)

        setSupportActionBar(binding.mainToolbar)

        navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_categories, R.id.nav_cart, R.id.nav_profile
            )
        )

        //setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        binding.mainToolbar.setupWithNavController(navController, appBarConfiguration)

        binding.scanBarCode.setOnClickListener {
            navController.navigate(R.id.nav_barcodeScanner)
        }

        binding.favItems.setOnClickListener {
            navController.navigate(R.id.nav_myFavouriteItems)
        }

        binding.searchItems.setOnClickListener {
            when(navController.currentDestination?.id)
            {
                R.id.nav_home->
                    navController.navigate(HomeFragmentDirections.actionNavHomeToNavSearch())
                R.id.nav_categories->
                    navController.navigate(CategoryFragmentDirections.actionNavCategoriesToNavSearch())
                R.id.nav_subCategories->
                    navController.navigate(SubCategoriesFragmentDirections.actionNavSubCategoriesToNavSearch())
                R.id.nav_selectedSubCategory->
                    navController.navigate(SelectedSubCategoryFragmentDirections.actionNavSelectedSubCategoryToNavSearch())
            }
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            hideSoftKeyboard()
            bottomNavigationView.show()
            binding.searchLay.hide()
            binding.imageView2.visibility = View.GONE
            supportActionBar?.show()
            when (destination.id) {
                R.id.nav_home,
                R.id.nav_categories  -> {
                    binding.searchLay.show()
                    binding.imageView2.visibility = View.VISIBLE
                }

                R.id.nav_cart, R.id.nav_profile -> {
                    binding.imageView2.visibility = View.VISIBLE
                }

                R.id.nav_checkOut,R.id.nav_addressList ,R.id.nav_selectedAddress,
                R.id.nav_myPaymentMethod ,R.id.nav_addAddress, R.id.nav_profileSettings,
                R.id.nav_myFavouriteItems, R.id.nav_barcodeScanner-> {
                    bottomNavigationView.hide()
                }

                R.id.nav_subCategories,R.id.nav_selectedSubCategory -> {
                    binding.searchLay.show()
                }
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener {item ->
            if(navController.currentDestination?.id != item.itemId) {
                onNavDestinationSelected(item, navController)
            }
            else false

        }
    }

    fun hideBottomNavigation()
    {
        bottomNavigationView.hide()
    }

    fun hideToolbar()
    {
        supportActionBar?.hide()
    }


    fun showBottomNavigation()
    {
        bottomNavigationView.show()
    }

    fun showToolbar()
    {
        supportActionBar?.show()
    }
    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.END)
    }

    fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.END)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        hideSoftKeyboard()
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        KeyboardEventListener(this) { isOpen ->
            if (isOpen) bottomNavigationView.hide()
        }

        when (navController.currentDestination?.id) {
            R.id.nav_home,R.id.nav_categories,R.id.nav_subCategories,
            R.id.nav_selectedSubCategory,R.id.nav_cart  -> {
                binding.searchLay.show()
                bottomNavigationView.show()
            }
            else -> bottomNavigationView.hide()

//            R.id.nav_addressList -> {
//                bottomNavigationView.hide()
//            }
//            R.id.nav_selectedAddress -> {
//                bottomNavigationView.hide()
//            }
//            R.id.nav_myPaymentMethod -> {
//                bottomNavigationView.hide()
//            }
//            R.id.nav_addAddress -> {
//                bottomNavigationView.hide()
//            }
//            R.id.nav_profileSettings -> {
//                bottomNavigationView.hide()
//            }
//            R.id.nav_myFavouriteItems -> {
//                bottomNavigationView.hide()
//            }
//            R.id.nav_barcodeScanner -> {
//                bottomNavigationView.hide()
//            }

        }
    }

    fun getDrawer(): DrawerLayout {
        return binding.drawerFragment
    }

    fun setTitle(title: String)
    {
        binding.mainToolbar.title=title
    }

    override fun onBackPressed() {
        super.onBackPressed()
        hideSoftKeyboard()
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
    }

    override fun onDrawerOpened(drawerView: View) {
    }

    override fun onDrawerClosed(drawerView: View) {
        //filterFragmentSideSheet.changePage(0)
        (supportFragmentManager.findFragmentByTag("filterFragmentSideSheet") as FilterFragment).changePage(0)
    }

    override fun onDrawerStateChanged(newState: Int) {
    }

    override fun attachBaseContext(newContext: Context?) {
        val context= newContext?.let {  changeAppLocale( PrefManager.getLocale(it) , it) }
        Log.d("locale",PrefManager.getLocale(context!!))
        super.attachBaseContext(context)
    }

}