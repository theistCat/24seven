package a24seven.uz.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import a24seven.uz.databinding.FragmentFilterOptionsBinding

class FilterOptionsFragment : Fragment() {

    private var _binding: FragmentFilterOptionsBinding? = null
    private val binding get() = _binding!!
  //  private val parent = parentFragment as FilterFragment


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
        (parentFragment as FilterFragment).characteristics.observe(
            viewLifecycleOwner, Observer { characteristics ->
                characteristics?.let {
                    adapter.updateList(it)
                }
            }
        )
        setUpClickListeners()
        return binding.root
    }


    fun setUpClickListeners() {

        binding.resetFilter.setOnClickListener {
            (parentFragment as FilterFragment).filter.clear()
            (parentFragment as FilterFragment).resetFilter = true
            adapter.notifyDataSetChanged()
        }

        binding.apply.setOnClickListener {

        }
    }

    private fun setUpAdapter() {
        adapter = FilterOptionsAdapter(requireContext())
       // adapter.updateList((parentFragment as FilterFragment).characteristics)

        adapter.onItemClick = {
            (parentFragment as FilterFragment).selectedOptionId = it.id
            (parentFragment as FilterFragment).selectedOptionTitle = it.name
            (parentFragment as FilterFragment).selectedOption.value = it.attributes
            (parentFragment as FilterFragment).changePage(1)
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::adapter.isInitialized) {
            adapter.filter = (parentFragment as FilterFragment).filter
            adapter.notifyDataSetChanged()
        }
    }

    private fun setUpRecycler() {
        binding.filterOptionsRecycler.adapter = adapter
        binding.filterOptionsRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

}