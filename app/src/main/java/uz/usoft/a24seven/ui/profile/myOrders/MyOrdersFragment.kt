package uz.usoft.a24seven.ui.profile.myOrders

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_my_orders.*
import uz.usoft.a24seven.R

class MyOrdersFragment : Fragment() {


    private lateinit var pagerAdapter: MyOrdersPagerAdapter
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
        return inflater.inflate(R.layout.fragment_my_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pagerAdapter=MyOrdersPagerAdapter(this)
        pager.adapter=pagerAdapter
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.in_wait)
                }
                1 -> {
                    tab.text = getString(R.string.active)
                }
                2->{
                    tab.text=getString(R.string.delivered)
                }
            }
        }.attach()

        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(),R.color.tabInactiveOrder))
        tabLayout.setSelectedTabIndicator(R.drawable.pill_indicator)
        tabLayout.setSelectedTabIndicatorGravity(TabLayout.INDICATOR_GRAVITY_CENTER)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position)
                {
                    0 -> {
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(),R.color.tabInactiveOrder))
                    }
                    1 -> {
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(),R.color.tabActiveOrder))
                    }
                    2->{
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(),R.color.tabDeliveredOrder))
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when(tab?.position)
                {
                    0 -> {
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(),R.color.tabInactiveOrder))
                    }
                    1 -> {
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(),R.color.tabActiveOrder))
                    }
                    2->{
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(),R.color.tabDeliveredOrder))
                    }
                }

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when(tab?.position)
                {
                    0 -> {
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(),R.color.tabInactiveOrder))
                    }
                    1 -> {
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(),R.color.tabActiveOrder))
                    }
                    2->{
                        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(),R.color.tabDeliveredOrder))
                    }
                }
            }

        })
    }

}