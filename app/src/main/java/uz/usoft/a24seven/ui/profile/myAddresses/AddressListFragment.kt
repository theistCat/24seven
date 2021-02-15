package uz.usoft.a24seven.ui.profile.myAddresses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_address_list.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.toDpi

class AddressListFragment : Fragment() {

    private lateinit var adapter: AddressListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter= AddressListAdapter()
        addressRecycler.adapter=adapter
        addressRecycler.layoutManager=LinearLayoutManager(requireContext())
        addressRecycler.addItemDecoration(SpacesItemDecoration(toDpi(16),true,1))

        adapter.onItemClick={
            findNavController().navigate(R.id.action_nav_addressList_to_selectedAddressFragment)
        }

        addAddress.setOnClickListener {
            findNavController().navigate(R.id.action_nav_addressList_to_nav_addAddress)
        }

    }
}