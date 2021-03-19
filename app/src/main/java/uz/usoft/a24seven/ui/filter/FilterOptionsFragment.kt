package uz.usoft.a24seven.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import uz.usoft.a24seven.databinding.FragmentFilterOptionsBinding

class FilterOptionsFragment : Fragment() {

    private var _binding: FragmentFilterOptionsBinding? = null
    private val binding get() = _binding!!
   // private val parent = parentFragment as FilterFragment


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
        _binding = FragmentFilterOptionsBinding.inflate(inflater, container, false)
        setUpRecycler()
        return binding.root
    }

    private fun setUpAdapter() {
        adapter = FilterOptionsAdapter()
        adapter.onItemClick = {
            (parentFragment as FilterFragment).changePage(1)
        }
    }

    private fun setUpRecycler() {
        binding.filterOptionsRecycler.adapter = adapter
        binding.filterOptionsRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

}