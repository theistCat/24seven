package a24seven.uz.utils

import a24seven.uz.MainActivity
import a24seven.uz.R
import a24seven.uz.databinding.FragmentCollectionObjectBinding
import a24seven.uz.network.models.Banner
import a24seven.uz.network.utils.Event
import a24seven.uz.ui.products.ImagesAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.NumberFormat
import java.util.Locale

fun Fragment.toDp(px: Int): Int {
    return ((requireContext().resources.displayMetrics.density * px) + 0.5f).toInt()
}

fun Activity.toDp(px: Int): Int {
    return ((this.resources.displayMetrics.density * px) + 0.5f).toInt()
}

fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

class SpacesItemDecoration(
    private val space: Int,
    private val vertical: Boolean = true,
    private val span: Int = 2
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if (vertical) {

            val itemposition = parent.getChildLayoutPosition(view)
            if (span > 1) {
                val column = itemposition % span

                outRect.left = space - column * space / span
                outRect.right = (column + 1) * space / span
                outRect.bottom = space

                if (itemposition < span) {
                    outRect.top = space
                } else {
                    outRect.top = 0
                }
            } else {
                outRect.left = space
                outRect.right = space
                outRect.bottom = space - space / 4

                if (itemposition == 0)
                    outRect.top = space
                else {
                    outRect.top = 0
                }
            }
        } else {
            val itemposition = parent.getChildLayoutPosition(view)

            outRect.top = 0
            outRect.bottom = 2
            outRect.right = space / 2

            if (itemposition == 0) {
                outRect.left = space
            } else {
                outRect.left = 0
            }
        }

    }
}

fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(
            Context
                .INPUT_METHOD_SERVICE
        ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}

fun Fragment.hideKeyboard() {
    activity?.hideSoftKeyboard()
}


fun <T> LifecycleOwner.observe(liveData: LiveData<T>, action: (t: T) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}

fun <T> LifecycleOwner.observeEvent(liveData: LiveData<Event<T>>, action: (t: Event<T>) -> Unit) {
    liveData.observe(this, Observer { it?.let { t -> action(t) } })
}

fun ImageView.image(
    context: Context,
    imageLink: String,
    placeholder: Int = R.drawable.ic__24seven_logo
) {
    Glide.with(context).load(imageLink).override(1000)
        .placeholder(placeholder)
        .into(this)
}

/**
 * Transforms static java function Snackbar.make() to an extension function on View.
 */
fun Fragment.showSnackbar(snackbarText: String, timeLength: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(requireView(), snackbarText, timeLength)
        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.snackbar))
        .setActionTextColor(ContextCompat.getColor(requireContext(), android.R.color.white)).show()
}

fun Activity.showSnackbar(snackbarText: String, timeLength: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this.getRootView(), snackbarText, timeLength)
        .setBackgroundTint(ContextCompat.getColor(this, R.color.snackbar))
        .setActionTextColor(ContextCompat.getColor(this, android.R.color.white)).show()
}

fun Fragment.showActionSnackbar(
    snackbarText: String,
    actionText: String,
    action: () -> Unit,
    timeLength: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(requireView(), snackbarText, timeLength)
        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.snackbar))
        .setAction(actionText) {
            action.invoke()
        }
        .setActionTextColor(ContextCompat.getColor(requireContext(), android.R.color.white)).show()
}


fun View.showAsProgress() {
    val loadingAnimation = AnimationUtils.loadAnimation(this.context, R.anim.spinning)
    this.startAnimation(loadingAnimation)
    this.visibility = View.VISIBLE
}

fun View.hideProgress() {
    this.clearAnimation()
    this.visibility = View.INVISIBLE
}

/**
 * navigate with animations.
 * has one animation at the moment,
 * later planed to add more with param to choose one
 *
 * @return nothing
 */
fun Fragment.navigate(resId: Int, args: Bundle? = null) {

    val builder = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in)
        .setExitAnim(R.anim.fade_out)
        .setPopEnterAnim(R.anim.fade_in)
        .setPopExitAnim(R.anim.slide_out)

    this.findNavController().navigate(resId, args, builder.build())
}

fun Fragment.navigate(action: NavDirections, slowLoad: Boolean = false) {

    val builder = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in)
        .setExitAnim(R.anim.fade_out)
        .setPopEnterAnim(R.anim.fade_in)
        .setPopExitAnim(R.anim.slide_out)

    this.findNavController().navigate(action, builder.build())
}

//fun EditText.formatPhoneMask() {
//    val affineFormats: MutableList<String> = ArrayList()
//    affineFormats.add("+998 [00] [000] [00] [00]")
//
//    val listener =
//        installOn(
//            this,
//            "+998 [00] [000] [00] [00]",
//            affineFormats, AffinityCalculationStrategy.WHOLE_STRING,
//            object : MaskedTextChangedListener.ValueListener {
//                override fun onTextChanged(
//                    maskFilled: Boolean,
//                    extractedValue: String,
//                    formattedValue: String
//                ) {
//                }
//            }
//        )
//}


class ImageCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    var fragmentImageList = ArrayList<Fragment>()

    fun updateImageList(imageList: ArrayList<Banner>) {
        fragmentImageList.clear()
        imageList.forEach {
            fragmentImageList.add(ImageObjectFragment.newInstance(it))
        }
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = fragmentImageList.size

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        return fragmentImageList[position]
    }
}

private const val ARG_IMG_LINK = "imageLink"
private const val ARG_IMG = "image"


class ImageObjectFragment() : Fragment() {

    private var _binding: FragmentCollectionObjectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionObjectBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    var onBannerClick: (() -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            val image = it.getString(ARG_IMG)
            val imageLink = it.getString(ARG_IMG_LINK)
            Glide.with(requireContext()).load(image).placeholder(R.drawable.banner)
                .into(binding.imageView)
            binding.imageView.setOnClickListener {
                Log.d("banner", imageLink?.matches(Regex("(https*:\\/\\/[a-z]+.*)")).toString())
                if (imageLink?.matches(Regex("(https*:\\/\\/[a-z]+.*)")) == true) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(imageLink)
                    startActivity(intent)
                }
            }
        }
    }

    companion object {
        fun newInstance(banner: Banner): ImageObjectFragment {
            val fragment = ImageObjectFragment()
            fragment.arguments = Bundle().apply {
                // Our object is just an integer :-P
                putString(ARG_IMG, banner.image)
                putString(ARG_IMG_LINK, banner.link)
            }
            return fragment
        }
    }
}

fun Fragment.setUpViewPager(
    adapter: ImageCollectionAdapter,
    viewPager: ViewPager2,
    tabLayout: TabLayout
) {
    viewPager.adapter = adapter

    TabLayoutMediator(tabLayout, viewPager) { _, _ ->
    }.attach()

}

fun Fragment.setUpViewPager(
    adapter: ImagesAdapter,
    viewPager: ViewPager2,
    tabLayout: TabLayout
) {
    viewPager.adapter = adapter

    TabLayoutMediator(tabLayout, viewPager) { _, _ ->
    }.attach()

}


// Add these extension functions to an empty kotlin file
fun Activity.getRootView(): View {
    return findViewById<View>(android.R.id.content)
}

fun Context.convertDpToPx(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        this.resources.displayMetrics
    )
}

fun Activity.isKeyboardOpen(): Boolean {
    val visibleBounds = Rect()
    this.getRootView().getWindowVisibleDisplayFrame(visibleBounds)
    val heightDiff = getRootView().height - visibleBounds.height()
    val marginOfError = Math.round(this.convertDpToPx(50F))
    return heightDiff > marginOfError
}

fun Activity.isKeyboardClosed(): Boolean {
    return !this.isKeyboardOpen()
}

class KeyboardEventListener(
    private val activity: AppCompatActivity,
    private val callback: (isOpen: Boolean) -> Unit
) : LifecycleObserver {

    private val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
        private var lastState: Boolean = activity.isKeyboardOpen()

        override fun onGlobalLayout() {
            val isOpen = activity.isKeyboardOpen()
            if (isOpen == lastState) {
                return
            } else {
                dispatchKeyboardEvent(isOpen)
                lastState = isOpen
            }
        }
    }

    init {
        // Dispatch the current state of the keyboard
        dispatchKeyboardEvent(activity.isKeyboardOpen())
        // Make the component lifecycle aware
        activity.lifecycle.addObserver(this)
        registerKeyboardListener()
    }

    private fun registerKeyboardListener() {
        activity.getRootView().viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    private fun dispatchKeyboardEvent(isOpen: Boolean) {
        when {
            isOpen -> callback(true)
            !isOpen -> callback(false)
        }
    }

    @OnLifecycleEvent(value = Lifecycle.Event.ON_PAUSE)
    @CallSuper
    fun onLifecyclePause() {
        unregisterKeyboardListener()
    }

    private fun unregisterKeyboardListener() {
        activity.getRootView().viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }
}

/**
 * Animating view with slide animation
 */
fun View.slideDown() {
    this.animate().translationY(500f).alpha(0.0f).setDuration(50).setInterpolator(
        AccelerateDecelerateInterpolator()
    )
    this.hide()
}

fun View.slideUp() {
    this.show()
    this.animate().translationY(0f).alpha(1.0f).setDuration(150).setInterpolator(
        AccelerateDecelerateInterpolator()
    )
}

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
fun View.hide(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
        Log.d("DefaultTag", "actually hidden")
    }
    return this
}

fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}


fun Fragment.makePhoneCall(phone: String) {
    val call = Intent(Intent.ACTION_DIAL);
    call.data = Uri.parse("tel:$phone")
    requireActivity().startActivity(call)
}


fun EditText.hideErrorIfFilled() {
    this.doOnTextChanged { text, start, before, count ->
        if (this.text.toString().isNotEmpty() and !this.error.isNullOrEmpty()) {
            this.error = null
        }
    }
}

fun EditText.showErrorIfNotFilled(): Boolean {
    if (this.text.toString().isEmpty()) {
        this.error = context.getString(R.string.warning_fill_the_fields)
        return false
    }
    return true
}

fun EditText.showErrorIfNotFilled(lay: com.google.android.material.textfield.TextInputLayout): Boolean {
    if (this.text.toString().isEmpty()) {
        lay.error = context.getString(R.string.warning_fill_the_fields)
        return false
    }
    return true
}

fun EditText.showError() {
    this.error = context.getString(R.string.warning_fill_the_fields)
}

fun TextInputEditText.showError(error: String) {
    this.error = error
}

fun AppCompatButton.showError(error: String) {
    this.error = error
}

fun AppCompatButton.hideError() {
    this.error = null
}

fun Activity.changeAppLocale(localeCode: String, context: Context): Context {
    val resources = context.resources
    val config = resources.configuration
    config.setLocale(Locale(localeCode.toLowerCase(Locale.ROOT)))
    context.createConfigurationContext(config)
    return context
}


fun EditText.formatPhoneMask() {
    val affineFormats: MutableList<String> = ArrayList()
    affineFormats.add("+998 [00] [000] [00] [00]")

    val listener =
        MaskedTextChangedListener.installOn(
            this,
            "+998 [00] [000] [00] [00]",
            affineFormats, AffinityCalculationStrategy.WHOLE_STRING,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {
                }
            }
        )
}

fun EditText.getMaskedPhoneWithoutSpace(): String {
    var phone = this.text.toString()
    if (phone.startsWith("+"))
        phone = phone.substring(1, phone.length)
    return phone.replace(" ", "")
}

fun Fragment.changeUiStateEnabled(isLoading: Boolean, progressBar: View, viewButton: View) {
    viewButton.isEnabled = !isLoading
    if (isLoading) progressBar.visible() else progressBar.gone()
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}


fun TextView.formatCurrencyMask(price: Float) {
    val format = NumberFormat.getCurrencyInstance(Locale("ru", "UZ"))
    this.text = format.format(price)
}

fun Context.getDisplayMetrics(): DisplayMetrics {
    return (this as MainActivity).resources.displayMetrics
}

fun Fragment.createBottomSheet(layout: Int): BottomSheetDialog {
    val bottomSheetDialog = BottomSheetDialog(requireContext())
    bottomSheetDialog.setContentView(layout)
    val bottomSheetInternal = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet)
    BottomSheetBehavior.from<View?>(bottomSheetInternal!!).peekHeight =
        (requireContext().getDisplayMetrics().heightPixels * 0.8).toInt()

    return bottomSheetDialog
}

fun Fragment.createBottomSheet(view: View): BottomSheetDialog {
    val bottomSheetDialog = BottomSheetDialog(requireContext())
    bottomSheetDialog.setContentView(view)
    val bottomSheetInternal = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet)
    BottomSheetBehavior.from<View?>(bottomSheetInternal!!).peekHeight =
        (requireContext().getDisplayMetrics().heightPixels * 0.8).toInt()

    return bottomSheetDialog
}

/**
 * Intent to send a telegram message
 * @param msg
 */
fun Fragment.intentShare(msg: String?, app: Int, uri: Uri?) {
    var appName = when (app) {
        0 -> "org.telegram.messenger"
        1 -> "com.instagram.android"
        2 -> "com.facebook.katana"
        else -> "org.thunderdog.challegram"
    }

    var isAppInstalled: Boolean = isAppAvailable(appName, requireContext().packageManager)

    when (app) {
        0 -> {
            if (isAppAvailable("org.telegram.messenger.web", requireContext().packageManager)) {
                isAppInstalled = true
                appName = "org.telegram.messenger.web"
            }
        }

    }

    if (isAppInstalled) {
        val myIntent = Intent(Intent.ACTION_SEND)
        myIntent.type = if (uri != null) "image/*" else "text/plain"
        myIntent.setPackage(appName)
        myIntent.putExtra(Intent.EXTRA_TEXT, msg) //
        // Create the URI from the media

        myIntent.putExtra(Intent.EXTRA_STREAM, uri) //
        requireActivity().startActivity(Intent.createChooser(myIntent, "Share with"))
    } else {
        Toast.makeText(requireContext(), "App not Installed", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Intent to send a telegram message
 * @param msg
 */
fun Fragment.intentShareInstagram(msg: String?, app: Int) {
    if (isAppAvailable("com.instagram.android", requireContext().packageManager)) {
        val MEDIA_TYPE_JPEG = "image/jpg"

        // Define image asset URI
        val backgroundAssetUri = Uri.parse("your-image-asset-uri-goes-here")
        val sourceApplication = "com.my.app"


        // Instantiate implicit intent with ADD_TO_STORY action and background asset
        val intent = Intent("com.instagram.share.ADD_TO_STORY")

        intent.setDataAndType(backgroundAssetUri, MEDIA_TYPE_JPEG)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        // Instantiate activity and verify it will resolve implicit intent

        // Instantiate activity and verify it will resolve implicit intent
        val activity: Activity? = activity
        if (activity!!.packageManager.resolveActivity(intent, 0) != null) {
            activity.startActivityForResult(intent, 0)
        }
    } else {
        Toast.makeText(
            requireContext(),
            getString(R.string.app_not_installed),
            Toast.LENGTH_SHORT
        ).show()
    }
}

private fun isAppAvailable(packageName: String, packageManager: PackageManager): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Fragment.getImageUri(inImage: Bitmap): Uri? {
//    val bytes = ByteArrayOutputStream()
//    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//    val path = MediaStore.Images.Media.insertImage(
//        requireActivity().contentResolver,
//        inImage,
//        UUID.randomUUID().toString() + ".png",
//        "drawing"
//    )
//    return Uri.parse(path)
    try {
        val cachePath = File(requireContext().cacheDir, "images")
        cachePath.mkdirs() // don't forget to make the directory
        val stream =
            FileOutputStream("$cachePath/image.png") // overwrites this image every time
        inImage.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    val imagePath = File(requireContext().cacheDir, "images")
    val newFile = File(imagePath, "image.png")
    val contentUri: Uri =
        FileProvider.getUriForFile(requireContext(), "uz.usoft.a24seven.fileprovider", newFile)
    return contentUri
}

//fragment_collection_object xml
//
//<?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//xmlns:app="http://schemas.android.com/apk/res-auto"
//xmlns:tools="http://schemas.android.com/tools"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//>
//
//<androidx.cardview.widget.CardView
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//app:cardCornerRadius="20dp"
//app:layout_constraintBottom_toBottomOf="parent"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toTopOf="parent">
//
//<ImageView
//android:id="@+id/imageView"
//android:layout_width="match_parent"
//android:layout_height="wrap_content"
//android:scaleType="centerCrop"
//app:layout_constraintBottom_toBottomOf="parent"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintHorizontal_bias="0.0"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toTopOf="parent"
//app:layout_constraintVertical_bias="0.006"
///>
//</androidx.cardview.widget.CardView>
//
//</androidx.constraintlayout.widget.ConstraintLayout>


//setting up a spinner

//val spinner: Spinner = sortSpinner
//
//
//ArrayAdapter.createFromResource(
//requireContext(),
//R.array.sortParams,
//R.layout.spinner_lay
//).also { adapter ->
//    // Specify the layout to use when the list of choices appears
//    adapter.setDropDownViewResource(R.layout.spinner_dropdown_lay)
//    // Apply the adapter to the spinner
//    spinner.adapter = adapter
//}