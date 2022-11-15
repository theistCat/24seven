package a24seven.uz.ui.profile.myOrders

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import a24seven.uz.ui.profile.myOrders.orderLists.MyActiveOrdersListFragment
import a24seven.uz.ui.profile.myOrders.orderLists.MyCanceledOrdersListFragment
import a24seven.uz.ui.profile.myOrders.orderLists.MyDeliveredOrdersListFragment
import a24seven.uz.ui.profile.myOrders.orderLists.MyInactiveOrdersListFragment

class MyOrdersPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                MyActiveOrdersListFragment()
            }

            else -> {
                MyDeliveredOrdersListFragment()
            }

//            2 -> {
//                MyInactiveOrdersListFragment()
//            }
//
//            else ->
//                MyCanceledOrdersListFragment()
        }
    }

}