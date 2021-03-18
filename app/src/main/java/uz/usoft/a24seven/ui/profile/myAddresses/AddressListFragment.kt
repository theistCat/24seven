package uz.usoft.a24seven.ui.profile.myAddresses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentAddressListBinding
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.toDp

class AddressListFragment : Fragment() {

    private var _binding: FragmentAddressListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AddressListAdapter
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
        _binding = FragmentAddressListBinding.inflate(inflater, container, false)
        setUpRecyclerView()
        setUpClickListener()
        return binding.root
    }

    private fun setUpAdapter() {
        adapter = AddressListAdapter()
        adapter.onItemClick = {
            findNavController().navigate(R.id.action_nav_addressList_to_selectedAddressFragment)
        }
    }

    private fun setUpRecyclerView() {
        binding.addressRecycler.adapter = adapter
        binding.addressRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.addressRecycler.addItemDecoration(SpacesItemDecoration(toDp(16), true, 1))
    }

    private fun setUpClickListener() {
        binding.addAddress.setOnClickListener {
            findNavController().navigate(R.id.action_nav_addressList_to_nav_addAddress)
        }
    }
}