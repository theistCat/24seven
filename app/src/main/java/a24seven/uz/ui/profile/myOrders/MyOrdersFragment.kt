package a24seven.uz.ui.profile.myOrders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import a24seven.uz.R
import a24seven.uz.databinding.FragmentMyOrdersBinding

class MyOrdersFragment : Fragment() {

    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentMyOrdersBinding.inflate(inflater, container, false)
        setUpPager()
        setUpTabLayouts()
        return binding.root
    }

    private fun setUpPager() {

        pagerAdapter = MyOrdersPagerAdapter(this)
        binding.pager.adapter = pagerAdapter

    }


    private fun setUpTabLayouts() {


        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                2 -> {
                    tab.text = getString(R.string.in_wait)
                }

                0 -> {
                    tab.text = getString(R.string.active)
                }

                1 -> {
                    tab.text = getString(R.string.delivered)
                }

                3 -> {
                    tab.text = getString(R.string.canceled)
                }

            }
        }.attach()

        binding.tabLayout.setSelectedTabIndicatorColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.tabActiveOrder
            )
        )

        //for some reason this line is causing a bug where indicator is not visible
        //binding.tabLayout.setSelectedTabIndicator(R.drawable.pill_indicator)

        binding.tabLayout.setSelectedTabIndicatorGravity(TabLayout.INDICATOR_GRAVITY_CENTER)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    2 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabInactiveOrder
                            )
                        )
                    }

                    0 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabActiveOrder
                            )
                        )
                    }

                    1 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabDeliveredOrder
                            )
                        )
                    }

                    3 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabCanceledOrder
                            )
                        )
                    }

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    2 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabInactiveOrder
                            )
                        )
                    }

                    0 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabActiveOrder
                            )
                        )
                    }

                    1 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabDeliveredOrder
                            )
                        )
                    }

                    3 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabCanceledOrder
                            )
                        )
                    }

                }

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    2 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabInactiveOrder
                            )
                        )
                    }

                    0 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabActiveOrder
                            )
                        )
                    }

                    1 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabDeliveredOrder
                            )
                        )
                    }

                    3 -> {
                        binding.tabLayout.setSelectedTabIndicatorColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.tabCanceledOrder
                            )
                        )
                    }

                }
            }

        })
    }


}