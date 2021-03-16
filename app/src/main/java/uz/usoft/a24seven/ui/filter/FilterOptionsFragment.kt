package uz.usoft.a24seven.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_filter_options.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentFilterOptionsBinding

class FilterOptionsFragment : Fragment() {

    private var _binding: FragmentFilterOptionsBinding? = null
    private val binding get() = _binding!!
    private val parent = parentFragment as FilterFragment


    private lateinit var adapter: FilterOptionsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterOptionsBinding.inflate(inflater, cartRecycler, false)
        setUpRecycler()
        return binding.root
    }

    private fun setUpAdapter() {
        adapter = FilterOptionsAdapter()
        adapter.onItemClick = {
            parent.changePage(1)
        }
    }

    private fun setUpRecycler() {
        binding.filterOptionsRecycler.adapter = adapter
        binding.filterOptionsRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

}