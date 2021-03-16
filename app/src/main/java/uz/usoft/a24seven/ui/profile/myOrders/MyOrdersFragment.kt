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
import uz.usoft.a24seven.databinding.FragmentMyActiveOrdersListBinding
import uz.usoft.a24seven.databinding.FragmentMyOrdersBinding

class MyOrdersFragment : Fragment() {

    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerAdapter: MyOrdersPagerAdapter
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
        // Inflate the layout for this fragment
        _binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        setUpTabLayouts()
        return binding.root
    }

    private fun setUpAdapters() {
        pagerAdapter = MyOrdersPagerAdapter(this)
        binding.pager.adapter = pagerAdapter

    }

    private fun setUpTabLayouts() {
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.in_wait)
                }
                1 -> {
                    tab.text = getString(R.string.active)
                }
                2 -> {
                    tab.text = getString(R.string.delivered)
                }
            }
        }.attach()

        binding.tabLayout.setSelectedTabIndicatorColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.tabInactiveOrder
            )
        )
        binding.tabLayout.setSelectedTabIndicator(R.drawable.pill_indicator)
        binding.tabLayout.setSelectedTabIndicatorGravity(TabLayout.INDICATOR_GRAVITY_CENTER)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabInactiveOrder
                            )
                        )
                    }
                    1 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabActiveOrder
                            )
                        )
                    }
                    2 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabDeliveredOrder
                            )
                        )
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabInactiveOrder
                            )
                        )
                    }
                    1 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabActiveOrder
                            )
                        )
                    }
                    2 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabDeliveredOrder
                            )
                        )
                    }
                }

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabInactiveOrder
                            )
                        )
                    }
                    1 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabActiveOrder
                            )
                        )
                    }
                    2 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabDeliveredOrder
                            )
                        )
                    }
                }
            }

        })
    }


}