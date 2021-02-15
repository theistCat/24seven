package uz.usoft.a24seven

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import uz.usoft.a24seven.ui.filter.FilterFragment
import uz.usoft.a24seven.utils.KeyboardEventListener
import uz.usoft.a24seven.utils.hide
import uz.usoft.a24seven.utils.hideSoftKeyboard
import uz.usoft.a24seven.utils.show

class MainActivity : AppCompatActivity() ,DrawerLayout.DrawerListener{

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_24seven)
        setContentView(R.layout.activity_main)

        bottomNavigationView= findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawerFragment)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        drawerLayout.addDrawerListener(this)

        val toolbar: Toolbar =findViewById(R.id.main_toolbar)
//
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_categories, R.id.nav_cart,R.id.nav_profile))

        //setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        main_toolbar.setupWithNavController(navController,appBarConfiguration)

        scanBarCode.setOnClickListener {
            navController.navigate(R.id.nav_barcodeScanner)
        }

        favItems.setOnClickListener {
            navController.navigate(R.id.nav_myFavouriteItems)
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            hideSoftKeyboard()
            bottomNavigationView.show()
            searchLay.hide()
            toolbar.imageView2.visibility=View.GONE
            when(destination.id)
            {
                R.id.nav_home -> {
                    searchLay.show()
                    toolbar.imageView2.visibility = View.VISIBLE
                    //homeLogo.visibility= View.VISIBLE
                }
                R.id.nav_categories -> {
                    searchLay.show()
                    toolbar.imageView2.visibility = View.VISIBLE
                }
                R.id.nav_cart -> {
                    toolbar.imageView2.visibility = View.VISIBLE
                }

                R.id.nav_profile -> {
                    toolbar.imageView2.visibility = View.VISIBLE
                }
                R.id.nav_checkOut -> {
                    bottomNavigationView.hide()
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
                R.id.nav_subCategories -> {
                    searchLay.show()
                }
                R.id.nav_selectedSubCategory -> {
                    searchLay.show()
                }
            }
        }

    }


    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.END)
    }

    fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.END)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        hideSoftKeyboard()
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        KeyboardEventListener(this) { isOpen ->
            if(isOpen) bottomNavigationView.hide()
        }

        when(findNavController(R.id.nav_host_fragment).currentDestination?.id)
        {
            R.id.nav_home -> {
                searchLay.show()
            }
            R.id.nav_checkOut -> {
                bottomNavigationView.hide()
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
                searchLay.show()
            }
            R.id.nav_subCategories -> {
                searchLay.show()
            }
            R.id.nav_selectedSubCategory -> {
                searchLay.show()
            }
        }
    }

    fun getDrawer():DrawerLayout
    {
        return drawerFragment
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
        (filterFragmentSideSheet as FilterFragment).changePage(0)
    }

    override fun onDrawerStateChanged(newState: Int) {
    }

}