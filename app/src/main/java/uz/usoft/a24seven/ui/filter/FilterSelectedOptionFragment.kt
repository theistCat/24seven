package uz.usoft.a24seven.ui.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_filter_selected_option.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentFilterSelectedOptionBinding
import uz.usoft.a24seven.databinding.FragmentSelectedOrderBinding

class FilterSelectedOptionFragment : Fragment() {
    private var _binding: FragmentFilterSelectedOptionBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterSelectedOptionBinding.inflate(inflater, container, false)
        setUpOnClickListener()
        return binding.root
    }

    private fun setUpOnClickListener() {
        binding.selectedFilterOptionBack.setOnClickListener {
            (parentFragment as FilterFragment).changePage(0)
        }
    }
}