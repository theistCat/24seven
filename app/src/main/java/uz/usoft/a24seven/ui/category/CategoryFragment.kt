package uz.usoft.a24seven.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_category.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.utils.SpacesItemDecoration

class CategoryFragment : Fragment() {

    private lateinit var adapter:CategoriesListAdapter
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_category, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter= CategoriesListAdapter()
        categoryRecycler.adapter=adapter
        categoryRecycler.layoutManager=LinearLayoutManager(requireContext())
        categoryRecycler.addItemDecoration(SpacesItemDecoration((requireContext().resources.displayMetrics.density*16+0.5f).toInt(),true,1))


        adapter.onItemClick={
            val categoryName=it.name
            val action=CategoryFragmentDirections.actionNavCategoriesToSubCategoriesFragment(categoryName)
            findNavController().navigate(action)
        }
    }
}