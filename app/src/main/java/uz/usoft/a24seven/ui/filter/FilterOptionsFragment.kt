package uz.usoft.a24seven.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_filter_options.*
import uz.usoft.a24seven.R

class FilterOptionsFragment : Fragment() {

    private lateinit var adapter:FilterOptionsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_options, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= FilterOptionsAdapter()
        filterOptionsRecycler.adapter=adapter
        filterOptionsRecycler.layoutManager=LinearLayoutManager(requireContext())

        val parent= parentFragment as FilterFragment

        adapter.onItemClick={
            parent.changePage(1)
        }

    }
}