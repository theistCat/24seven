package a24seven.uz.ui.filter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import a24seven.uz.databinding.FragmentFilterSelectedOptionBinding

class FilterSelectedOptionFragment : Fragment() {
    private var _binding: FragmentFilterSelectedOptionBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SelectedFilterOptionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
    }

    fun setUpAdapter()
    {
        adapter= SelectedFilterOptionsAdapter()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterSelectedOptionBinding.inflate(inflater, container, false)
        setUpOnClickListener()
        setUpRecycler()

        val parent = (parentFragment as FilterFragment)
        (parentFragment as FilterFragment).selectedOption.observe(
            viewLifecycleOwner, Observer { characteristics ->
                characteristics?.let {
                    val key =parent.selectedOptionId.toString()
                    if(parent.filter[key]==null)
                        parent.filter[key]=ArrayList()
                    adapter.filter= parent.filter[key]
                    adapter.updateList(it)
                    binding.optionTitle.text=parent.selectedOptionTitle

                    adapter.onItemClick={ add, attribute->
                        if(add) {
                            (parent.filter[key])!!.add( attribute)
                        }
                        else
                        {
                            (parent.filter[key])!!.remove(attribute)
                            if(parent.filter[key]!!.isEmpty())
                                (parent.filter).remove(key)
                            Log.d("filterT"," remove ${parent.filter}")
                            if(parent.filter.isEmpty())
                            {
                                parent.resetFilter=true
                            }
                        }
                    }
                }
            }
        )
        return binding.root
    }

    private fun setUpRecycler() {

        binding.filterSelectedOptionsRecycler.adapter = adapter
        binding.filterSelectedOptionsRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUpOnClickListener() {
        binding.selectedFilterOptionBack.setOnClickListener {
            Log.i("filter",(parentFragment as FilterFragment).filter.toString())
            (parentFragment as FilterFragment).changePage(0)
        }
    }
}