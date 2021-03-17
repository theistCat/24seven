package uz.usoft.a24seven.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import kotlinx.android.synthetic.main.fragment_collection_object.*
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.kidya.data.PrefManager
import java.text.NumberFormat
import java.util.*

fun Fragment.toDpi(px:Int) :Int{
    return ((requireContext().resources.displayMetrics.density * px)+0.5f).toInt()
}


class SpacesItemDecoration(private val space: Int,private val vertical: Boolean=true,private val span:Int=2) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        if(vertical)
        {

            val itemposition = parent.getChildLayoutPosition(view)
            if(span>1) {
                val column = itemposition % span

                outRect.left = space - column * space / span
                outRect.right = (column + 1) * space / span
                outRect.bottom = space

                if (itemposition < span) {
                    outRect.top = space
                } else {
                    outRect.top = 0
                }
            }
            else{
                outRect.left = space
                outRect.right=space
                outRect.bottom = space-space/4

                if (itemposition == 0)
                    outRect.top = space
                else {
                    outRect.top = 0
                }
            }
        }
        else
        {
            val itemposition=parent.getChildLayoutPosition(view)

            outRect.top = 0
            outRect.bottom = 2
            outRect.right=space/2

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

    fun updateImageList(imageList: ArrayList<String>) {
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


class ImageObjectFragment() : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_collection_object, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            var imageLink = it.getString(ARG_IMG_LINK)
            Glide.with(requireContext()).load(imageLink).placeholder(R.drawable.banner).into(imageView)
        }
    }

    companion object {
        fun newInstance(imageLink: String): ImageObjectFragment {
            val fragment = ImageObjectFragment()
            fragment.arguments = Bundle().apply {
                // Our object is just an integer :-P
                putString(ARG_IMG_LINK, imageLink)
            }
            return fragment
        }
    }
}

fun Fragment.setUpViewPager(adapter:ImageCollectionAdapter,viewPager: ViewPager2,tabLayout: TabLayout)
{
    viewPager.adapter=adapter

    TabLayoutMediator(tabLayout, viewPager) { tab, position ->
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
            isOpen  -> callback(true)
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

fun EditText.showErrorIfNotFilled() {
    if (this.text.toString().isEmpty()) {
        this.error = context.getString(R.string.warning_fill_the_fields)
    }
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
/**
 * Change language
 */
fun Fragment.setAppLocale(localeCode: String, context: Context) {
    activity?.setAppLocale(localeCode, context)
}
fun Activity.setAppLocale(localeCode: String, context: Context) {
    val resources = context.resources
    val dm = resources.displayMetrics
    val config = resources.configuration
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        config.setLocale(Locale(localeCode.toLowerCase(Locale.ROOT)))
        PrefManager.saveLocale(context, localeCode.toLowerCase(Locale.ROOT))
    } else {
        PrefManager.saveLocale(context, localeCode.toLowerCase(Locale.ROOT))
        config.locale = Locale(localeCode.toLowerCase(Locale.ROOT))
    }
    resources.updateConfiguration(config, dm)
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
    val format= NumberFormat.getCurrencyInstance(Locale("ru", "UZ"))
    this.text=format.format(price)
}
fun Context.getDisplayMetrics(): DisplayMetrics {
    return (this as MainActivity).resources.displayMetrics
}

fun Fragment.createBottomSheet(layout: Int): BottomSheetDialog {
    val bottomSheetDialog = BottomSheetDialog(requireContext())
    bottomSheetDialog.setContentView(layout)
    val bottomSheetInternal = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet)
    BottomSheetBehavior.from<View?>(bottomSheetInternal!!).peekHeight = (requireContext().getDisplayMetrics().heightPixels*0.8).toInt()

    return bottomSheetDialog
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