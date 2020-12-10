package uz.usoft.a24seven.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.MaskedTextChangedListener.Companion.installOn
import com.redmadrobot.inputmask.helper.AffinityCalculationStrategy
import kotlinx.android.synthetic.main.fragment_collection_object.*
import uz.usoft.a24seven.R
import java.util.ArrayList

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
            var itemposition=parent.getChildLayoutPosition(view)
            var column= itemposition%span

            outRect.left = space-column* space/span
            outRect.right = (column+1)* space/span
            outRect.bottom = space

            if (itemposition < span ) {
                outRect.top = space
            } else {
                outRect.top = 0
            }
        }
        else
        {
            var itemposition=parent.getChildLayoutPosition(view)

            outRect.top = space
            outRect.bottom = space
            outRect.right=space

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

fun EditText.formatPhoneMask() {
    val affineFormats: MutableList<String> = ArrayList()
    affineFormats.add("+998 [00] [000] [00] [00]")

    val listener =
        installOn(
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