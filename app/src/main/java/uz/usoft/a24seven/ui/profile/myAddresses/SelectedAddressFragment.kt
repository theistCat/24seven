package uz.usoft.a24seven.ui.profile.myAddresses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentAddressListBinding
import uz.usoft.a24seven.databinding.FragmentSelectedAddressBinding

class SelectedAddressFragment : Fragment() {

    private var _binding: FragmentSelectedAddressBinding? = null
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
        _binding = FragmentSelectedAddressBinding.inflate(inflater, container, false)
        return binding.root
    }


}