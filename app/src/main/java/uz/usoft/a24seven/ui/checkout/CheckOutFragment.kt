package uz.usoft.a24seven.ui.checkout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_check_out.*
import uz.usoft.a24seven.R


class CheckOutFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner: Spinner = savedAddresses


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

        checkout.setOnClickListener{
            findNavController().navigate(R.id.action_nav_checkOut_to_nav_myOrders)
        }
    }
}