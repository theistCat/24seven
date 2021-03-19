package uz.usoft.a24seven.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setUpOnClick()
        return binding.root
    }

    private fun setUpOnClick() {
        binding.myOrders.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_nav_myOrders)
        }
        binding.myAddress.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_nav_addressList)
        }
        binding.myPaymentMethod.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_nav_myPaymentMethod)
        }

        binding.profileSettings.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_nav_profileSettings)
        }

        binding.myFavouriteItems.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_nav_myFavouriteItems)
        }
    }
}