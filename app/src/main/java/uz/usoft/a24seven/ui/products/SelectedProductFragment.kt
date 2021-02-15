package uz.usoft.a24seven.ui.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_selected_product.*
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.ui.category.selectedSubCategory.SelectedSubCategoryFragmentArgs
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.utils.*

class SelectedProductFragment : Fragment() {

    private lateinit var pagerAdapter: ImageCollectionAdapter
    private lateinit var similarItemAdapter:ProductsListAdapter
    private lateinit var feedbackListAdapter:FeedbackListAdapter
    //private lateinit var feedbacks:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selected_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs: SelectedProductFragmentArgs by navArgs()
        (requireActivity() as MainActivity).main_toolbar.title=safeArgs.selectedCategoryName

        similarItemAdapter= ProductsListAdapter()
        similarItemsRecycler.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        similarItemsRecycler.adapter=similarItemAdapter
        similarItemsRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16),false))


        feedbackListAdapter= FeedbackListAdapter()
        feedbackRecycler.adapter=feedbackListAdapter
        feedbackRecycler.layoutManager=LinearLayoutManager(requireContext())

        pagerAdapter= ImageCollectionAdapter(this)
        setUpViewPager(pagerAdapter,productPager,productTabLayout)

        var imgList=ArrayList<String>()

        imgList.add("https://i.imgur.com/0Qy.png")
        imgList.add("https://i.imgur.com/0Qy.png")
        imgList.add("https://i.imgur.com/0Qy.png")

        pagerAdapter.updateImageList(imgList)


        val feedbackBottomSheet=createBottomSheet(R.layout.feedback_bottomsheet)
        leaveFeedback.setOnClickListener {
            feedbackBottomSheet.show()
        }
    }
}