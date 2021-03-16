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
import uz.usoft.a24seven.databinding.FragmentSelectedProductBinding
import uz.usoft.a24seven.ui.category.selectedSubCategory.SelectedSubCategoryFragmentArgs
import uz.usoft.a24seven.ui.home.ProductsListAdapter
import uz.usoft.a24seven.utils.*

class SelectedProductFragment : Fragment() {

    private var _binding: FragmentSelectedProductBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: ImageCollectionAdapter
    private lateinit var similarItemAdapter: ProductsListAdapter
    private lateinit var feedbackListAdapter: FeedbackListAdapter
    private val safeArgs: SelectedProductFragmentArgs by navArgs()
    private val imgList = ArrayList<String>()
    private val feedbackBottomSheet = createBottomSheet(R.layout.feedback_bottomsheet)


    //private lateinit var feedbacks:
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapters()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectedProductBinding.inflate(inflater, container, false)
        setUpRecyclerAdapters()
        setUpData()
        setUpOnClick()
        return binding.root
    }

    private fun setUpAdapters() {
        similarItemAdapter = ProductsListAdapter()
        feedbackListAdapter = FeedbackListAdapter()
        pagerAdapter = ImageCollectionAdapter(this)
        pagerAdapter.updateImageList(imgList)
        setUpViewPager(pagerAdapter, productPager, productTabLayout)

    }

    private fun setUpRecyclerAdapters() {
        binding.similarItemsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.similarItemsRecycler.adapter = similarItemAdapter
        binding.similarItemsRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16), false))

        binding.feedbackRecycler.adapter = feedbackListAdapter
        binding.feedbackRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUpData() {
        (requireActivity() as MainActivity).main_toolbar.title = safeArgs.selectedCategoryName

        imgList.add("https://i.imgur.com/0Qy.png")
        imgList.add("https://i.imgur.com/0Qy.png")
        imgList.add("https://i.imgur.com/0Qy.png")
    }

    private fun setUpOnClick() {
        leaveFeedback.setOnClickListener {
            feedbackBottomSheet.show()
        }
    }
}