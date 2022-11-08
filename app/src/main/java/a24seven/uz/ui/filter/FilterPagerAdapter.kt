package a24seven.uz.ui.filter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FilterPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                FilterOptionsFragment()
            }
            1 -> {
                FilterSelectedOptionFragment()
            }
            else -> FilterOptionsFragment()
        }
    }
}