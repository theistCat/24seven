package a24seven.uz

//import com.google.firebase.messaging.FirebaseMessaging
import a24seven.uz.data.PrefManager
import a24seven.uz.databinding.ActivityMainBinding
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.network.utils.Resource
import a24seven.uz.ui.category.CategoryFragmentDirections
import a24seven.uz.ui.category.selectedSubCategory.SelectedSubCategoryFragmentDirections
import a24seven.uz.ui.category.subCategory.SubCategoriesFragmentDirections
import a24seven.uz.ui.filter.FilterFragment
import a24seven.uz.ui.home.HomeFragmentDirections
import a24seven.uz.ui.loader.LoaderDialogFragment
import a24seven.uz.ui.seach.SearchFragment
import a24seven.uz.utils.changeAppLocale
import a24seven.uz.utils.hide
import a24seven.uz.utils.hideSoftKeyboard
import a24seven.uz.utils.show
import a24seven.uz.utils.showSnackbar
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class MainActivity : AppCompatActivity(), DrawerLayout.DrawerListener {

    companion object {

        lateinit var openAuthActivityCustom: ActivityResultLauncher<Intent>
        lateinit var openSearchActivityCustom: ActivityResultLauncher<Intent>
        lateinit var openSpeechToText: ActivityResultLauncher<Intent>
        lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
        const val ACCESS_TOKEN = "access_token"
        const val SEARCH_RESULT = "searched_product_id"
        val RecordAudioRequestCode = 120

        private var speechRecognizer: SpeechRecognizer? = null
    }

    var loadingDialog = LoaderDialogFragment()
    private var _badge: BadgeDrawable? = null
    private val badge get() = _badge!!
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    private val mainViewModel: MainViewModel by viewModel()

    var onSearchResult: ((Int) -> Unit)? = null
    var onLocationGranted: ((Int) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_24seven)


        Log.d("loaderTag", "$loadingDialog")

//        PrefManager.saveToken(this,"")
//        if(PrefManager.getTheme(this)){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        }
//        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        FirebaseMessaging.getInstance().subscribeToTopic("all")
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCMTAG", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                //Hawk.put(Constants.FCM, token)

                // Log and toast
//                Log.d("FCMTAG", token.toString())
            })


        openAuthActivityCustom =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        Log.d(
                            ACCESS_TOKEN, "access_token : ${
                                result.data?.getStringExtra(
                                    ACCESS_TOKEN
                                ).toString()
                            }"
                        )
                        PrefManager.saveToken(
                            this,
                            result.data?.getStringExtra(ACCESS_TOKEN).toString()
                        )
                        navController.popBackStack(R.id.nav_home, false)
                        navController.popBackStack(R.id.nav_cart, false)
                        navController.navigate(R.id.nav_profile)
                    }

                    Activity.RESULT_CANCELED -> {
                        navController.popBackStack(R.id.nav_home, false)
                    }
                }
            }


        openSpeechToText =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        val results: ArrayList<String> =
                            result?.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                                ?: ArrayList()
                        if (navController.currentDestination?.id == R.id.nav_search) {
                            val navHost =
                                (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                            val searchFragment =
                                navHost.childFragmentManager.fragments[0] as SearchFragment
                            searchFragment.binding.searchQuery.setText(results.joinToString())
                        }
                    }

                    Activity.RESULT_CANCELED -> {

                    }
                }
            }


        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.


                } else {
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }

        requestPermissionLauncher.launch(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        openSearchActivityCustom =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        onSearchResult?.invoke(result.data?.getIntExtra(SEARCH_RESULT, 0) ?: 0)
                    }

                    Activity.RESULT_CANCELED -> {

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
                R.id.nav_home
            )
        )

//        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
        binding.mainToolbar.setupWithNavController(navController, appBarConfiguration)


//        binding.scanBarCode.setOnClickListener {
//            navController.navigate(R.id.nav_coin)
//
//        }

        binding.adressItems.setOnClickListener {
            navController.navigate(R.id.nav_addressList)
//            val dialog = BottomSheetDialog(this)
//
//            // on below line we are inflating a layout file which we have created.
//            val view = layoutInflater.inflate(R.layout.fragment_address_list, null)
//
//            // on below line we are creating a variable for our button
//            // which we are using to dismiss our dialog.
////        val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)
////
////        // on below line we are adding on click listener
////        // for our dismissing the dialog button.
////        btnClose.setOnClickListener {
////            // on below line we are calling a dismiss
////            // method to close our dialog.
////            dialog.dismiss()
////        }
//            // below line is use to set cancelable to avoid
//            // closing of dialog box when clicking on the screen.
//            dialog.setCancelable(true)
//
//            // on below line we are setting
//            // content view to our view.
//            dialog.setContentView(view)
//
//            // on below line we are calling
//            // a show method to display a dialog.
//            dialog.show()
        }

        binding.searchItems.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.nav_home ->
                    navController.navigate(HomeFragmentDirections.actionNavHomeToNavSearch())

                R.id.nav_categories ->
                    navController.navigate(CategoryFragmentDirections.actionNavCategoriesToNavSearch())

                R.id.nav_subCategories ->
                    navController.navigate(SubCategoriesFragmentDirections.actionNavSubCategoriesToNavSearch())

                R.id.nav_selectedSubCategory ->
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
                R.id.nav_categories -> {
                    binding.searchLay.show()
                    binding.imageView2.visibility = View.VISIBLE
                }

                R.id.nav_cart, R.id.nav_profile -> {
                    binding.imageView2.visibility = View.VISIBLE
                }

                R.id.nav_checkOut, R.id.nav_addressList, R.id.nav_selectedAddress,
                R.id.nav_myPaymentMethod, R.id.nav_addAddress, R.id.nav_profileSettings,
                R.id.nav_myFavouriteItems, R.id.nav_barcodeScanner -> {
                    bottomNavigationView.hide()
                }

                R.id.nav_subCategories, R.id.nav_selectedSubCategory -> {
                    binding.searchLay.show()
                }
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            if (navController.currentDestination?.id != item.itemId) {
                onNavDestinationSelected(item, navController)
            } else false

        }


        _badge = bottomNavigationView.getOrCreateBadge(R.id.nav_cart)

        badge.isVisible = false
        badge.number = 0
        val uiMode = resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)
        if (uiMode == Configuration.UI_MODE_NIGHT_YES)
            badge.backgroundColor =
                ContextCompat.getColor(applicationContext, R.color.color_secondary)
        else
            badge.backgroundColor = ContextCompat.getColor(applicationContext, R.color.snackbar)

        badge.badgeTextColor = Color.WHITE

        mainViewModel.cart.observe(
            this, Observer { products ->
                products?.let {
                    updateBadge(it.size)
                }
            }
        )


        mainViewModel.getCoinResponse.observe(this, Observer {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
//                        binding.coins.text = resource.data.toString()
                    }

                    is Resource.GenericError -> {
                        showSnackbar(
                            resource.errorResponse.jsonResponse.optString("error") ?: "error"
                        )
                    }

                    is Resource.Error -> {
                        if (resource.exception is NoConnectivityException) {
                            //showNoConnectionDialog(this::onRetry)
                        } else resource.exception.message?.let { it1 -> showSnackbar(it1) }
                    }
                }
            }
        })

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale("ru"));


    }

    fun showLoadingDialog() {

        val fm = supportFragmentManager
        try {
            fm.executePendingTransactions()
        } catch (_: IllegalStateException) {
        }
        Log.d("loaderTag", "${fm.findFragmentByTag("Loading")?.isAdded}")
        if (!(loadingDialog.isAdded)) {
            loadingDialog.show(fm, "Loading")
            loadingDialog.isCancelable = false
        }
    }

    fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }


    fun updateBadge(count: Int) {
        badge.number = count
        if (badge.number < 1) {
            badge.isVisible = false
            badge.clearNumber()
        } else badge.isVisible = true
    }

    fun hideBottomNavigation() {
        bottomNavigationView.hide()
    }

    fun hideToolbar() {
        supportActionBar?.hide()
    }


    fun showBottomNavigation() {
        bottomNavigationView.show()
    }

    fun showToolbar() {
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

//        if (PrefManager.isLoggedIn(this))
//            mainViewModel.getCoins()
//        else binding.coins.text = ""
//        KeyboardEventListener(this) { isOpen ->
//            if (isOpen) bottomNavigationView.hide()
//        }

        when (navController.currentDestination?.id) {
            R.id.nav_home, R.id.nav_categories, R.id.nav_subCategories,
            R.id.nav_selectedSubCategory, R.id.nav_cart -> {
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

    fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onDestroy() {
        super.onDestroy()
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
        (supportFragmentManager.findFragmentByTag("filterFragmentSideSheet") as FilterFragment).changePage(
            0
        )
    }

    override fun onDrawerStateChanged(newState: Int) {
    }

    override fun attachBaseContext(newContext: Context?) {
        val context = newContext?.let { changeAppLocale(PrefManager.getLocale(it), it) }
        Log.d("locale", PrefManager.getLocale(context!!))
        super.attachBaseContext(context)
    }

}