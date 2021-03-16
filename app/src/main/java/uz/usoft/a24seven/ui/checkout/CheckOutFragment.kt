package uz.usoft.a24seven.ui.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentCheckOutBinding


class CheckOutFragment : Fragment() {

    private var _binding: FragmentCheckOutBinding? = null
    private val binding get() = _binding!!
    private val spinner: Spinner = binding.savedAddresses

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        setUpSpinnerAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckOutBinding.inflate(inflater, container, false)
        setUpClickLister()
        return binding.root
    }

    private fun setUpSpinnerAdapter() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.savedAddr,
            R.layout.spinner_lay
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_lay)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
    }

    private fun setUpClickLister() {
        binding.checkout.setOnClickListener {
            findNavController().navigate(R.id.action_nav_checkOut_to_nav_myOrders)
        }
    }
}