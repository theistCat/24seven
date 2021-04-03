package uz.usoft.a24seven

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
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

class MainActivity : AppCompatActivity(), DrawerLayout.DrawerListener {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_24seven)

        PrefManager

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       

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
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_backicon)

        binding.scanBarCode.setOnClickListener {
            navController.navigate(R.id.nav_barcodeScanner)
        }

        binding.favItems.setOnClickListener {
            navController.navigate(R.id.nav_myFavouriteItems)
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
                    //bottomNavigationView.hide()
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
            R.id.nav_home -> {
                binding.searchLay.show()
            }
            R.id.nav_addressList -> {
                bottomNavigationView.hide()
            }
            R.id.nav_selectedAddress -> {
                bottomNavigationView.hide()
            }
            R.id.nav_myPaymentMethod -> {
                bottomNavigationView.hide()
            }
            R.id.nav_addAddress -> {
                bottomNavigationView.hide()
            }
            R.id.nav_profileSettings -> {
                bottomNavigationView.hide()
            }
            R.id.nav_myFavouriteItems -> {
                bottomNavigationView.hide()
            }
            R.id.nav_barcodeScanner -> {
                bottomNavigationView.hide()
            }
            R.id.nav_categories -> {
                binding.searchLay.show()
            }
            R.id.nav_subCategories -> {
                binding.searchLay.show()
            }
            R.id.nav_selectedSubCategory -> {
                binding.searchLay.show()
            }
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
        Log.i("drawer", "opened")
    }

    override fun onDrawerClosed(drawerView: View) {
        Log.i("drawer", "closed")
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