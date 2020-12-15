package uz.usoft.a24seven.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_profile.*
import uz.usoft.a24seven.R

class ProfileFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myOrders.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_nav_myOrders)
        }
        myAddress.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_nav_addressList)
        }
        myPaymentMethod.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_nav_myPaymentMethod)
        }

        profileSettings.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_nav_profileSettings)
        }
    }
}