package uz.usoft.a24seven

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.abs_layout.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_24seven)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        //val bottomBar:BottomAppBar=findViewById(R.id.bottomAppBar)
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.abs_layout)
        supportActionBar?.elevation=0f
        supportActionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.appbar_background))
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_dashboard, R.id.nav_cart,R.id.nav_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id)
            {
                R.id.nav_home->{
                    tvTitle.visibility=View.GONE
                    imageView2.visibility=View.VISIBLE
                  //  homeLogo.visibility= View.VISIBLE
                }
                else->{

                    tvTitle.visibility=View.VISIBLE
                    tvTitle.text=destination.label
                    imageView2.visibility=View.GONE
                  ///  homeLogo.visibility= View.GONE
                }
            }
        }
    }
}