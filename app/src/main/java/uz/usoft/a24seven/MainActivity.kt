package uz.usoft.a24seven

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.abs_layout.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import uz.usoft.a24seven.ui.category.CategoryFragmentDirections
import uz.usoft.a24seven.ui.category.subCategory.SubCategoriesFragmentArgs
import uz.usoft.a24seven.utils.KeyboardEventListener
import uz.usoft.a24seven.utils.hide
import uz.usoft.a24seven.utils.show

class MainActivity : AppCompatActivity() {


    private lateinit var bottomNavigationView: BottomNavigationView

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_24seven)
        setContentView(R.layout.activity_main)

        bottomNavigationView= findViewById(R.id.nav_view)

        val appBar: AppBarLayout=findViewById(R.id.main_appBarLayout)
        val toolbar: Toolbar =findViewById(R.id.main_toolbar)
//
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       // val appBarConfiguration = AppBarConfiguration(setOf(
        //        R.id.nav_home, R.id.nav_dashboard, R.id.nav_cart,R.id.nav_profile))

        //setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        main_toolbar.setupWithNavController(navController)


        scanBarCode.setOnClickListener {
            navController.navigate(R.id.nav_barcodeScanner)
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            bottomNavigationView.show()
            searchLay.hide()
            toolbar.imageView2.visibility=View.GONE
            when(destination.id)
            {
                R.id.nav_home->{
                    searchLay.show()
                    toolbar.title=""
                    toolbar.imageView2.visibility=View.VISIBLE
                    //homeLogo.visibility= View.VISIBLE
                }
                R.id.nav_checkOut->{
                    bottomNavigationView.hide()
                }
                R.id.nav_addressList->{
                    bottomNavigationView.hide()
                }
                R.id.nav_selectedAddress->{
                    bottomNavigationView.hide()
                }
                R.id.nav_myPaymentMethod->{
                    bottomNavigationView.hide()
                }
                R.id.nav_addAddress->{
                    bottomNavigationView.hide()
                }
                R.id.nav_profileSettings->{
                    bottomNavigationView.hide()
                }
                R.id.nav_myFavouriteItems->{
                    bottomNavigationView.hide()
                }
                R.id.nav_categories->{
                    searchLay.show()
                }
                R.id.nav_subCategories->{
                    searchLay.show()
                }
                R.id.nav_selectedSubCategory->{
                    searchLay.show()
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
    override fun onResume() {
        super.onResume()
        KeyboardEventListener(this) { isOpen ->
            if(isOpen) bottomNavigationView.hide()
            else bottomNavigationView.show()
        }
    }
}