package uz.usoft.a24seven.ui.filter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_filter.*
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R

class FilterFragment() : Fragment(){

    private lateinit var pagerAdapter: FilterPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pagerAdapter= FilterPagerAdapter(this)
        filterPager.adapter=pagerAdapter
        filterPager.isUserInputEnabled=false
        filterPager.setCurrentItem(0,true)

        //val drawer=(requireActivity() as MainActivity).drawerLayout

        //drawer.addDrawerListener(this)
       // val drawer1=(requireActivity() as MainActivity).drawerLayout

    }

    fun changePage(page: Int=0)
    {
        filterPager.setCurrentItem(page,true)
    }

}