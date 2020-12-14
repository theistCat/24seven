package uz.usoft.a24seven.ui.profile.myOrders

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.usoft.a24seven.ui.profile.myOrders.orderLists.MyActiveOrdersListFragment
import uz.usoft.a24seven.ui.profile.myOrders.orderLists.MyDeliveredOrdersListFragment
import uz.usoft.a24seven.ui.profile.myOrders.orderLists.MyInactiveOrdersListFragment

class MyOrdersPagerAdapter (fragment: Fragment):FragmentStateAdapter(fragment){

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                MyInactiveOrdersListFragment()
            }
            1 -> {
                MyActiveOrdersListFragment()
            }
            2 -> {
                MyDeliveredOrdersListFragment()
            }
            else->
                MyInactiveOrdersListFragment()
        }
    }

}