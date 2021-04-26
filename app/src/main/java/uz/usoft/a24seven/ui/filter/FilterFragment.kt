package uz.usoft.a24seven.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import uz.usoft.a24seven.databinding.FragmentFilterBinding
import uz.usoft.a24seven.network.models.Characteristics

class FilterFragment() : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: FilterPagerAdapter
    val characteristics= MutableLiveData<List<Characteristics>>()
    val selectedOption= MutableLiveData<List<String>>()
    var selectedOptionTitle:String=""
    var selectedOptionId:Int=0

    val filter=HashMap<String,ArrayList<String>>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpPagerAdapter()

    }

    private fun setUpPagerAdapter() {
        pagerAdapter = FilterPagerAdapter(this)
        binding.filterPager.adapter = pagerAdapter
        binding.filterPager.isUserInputEnabled = false
        binding.filterPager.setCurrentItem(0, true)
    }


    fun changePage(page: Int = 0) {
        binding.filterPager.setCurrentItem(page, true)
    }

}