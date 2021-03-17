package uz.usoft.a24seven.ui.filter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_filter.*
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentFilterBinding

class FilterFragment() : Fragment() {

    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: FilterPagerAdapter

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
        filterPager.setCurrentItem(page, true)
    }

}