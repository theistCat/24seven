package uz.usoft.a24seven.ui.profile.myAddresses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentAddressListBinding
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.ui.profile.ProfileViewModel
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.SpacesItemDecoration
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.toDp

class AddressListFragment : BaseFragment<FragmentAddressListBinding>(FragmentAddressListBinding::inflate) {


    private val viewModel: ProfileViewModel by viewModel()
    private lateinit var adapter: AddressListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
        getAddresses()
    }


    override fun setUpObservers() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                //progressBar.isVisible = loadStates.refresh is LoadState.Loading
                //retry.isVisible = loadState.refresh !is LoadState.Loading
                when(loadStates.refresh)
                {
                    is LoadState.Error->{
                        val error = loadStates.refresh as LoadState.Error
                        if (error.error is NoConnectivityException)
                        {
                            showNoConnectionDialog(this@AddressListFragment::onRetry)
                        }
                    }
                    is LoadState.Loading->{
                        hideNoConnectionDialog()
                        showLoadingDialog()
                    }
                    else->{
                        hideNoConnectionDialog()
                        hideLoadingDialog()
                    }
                }

            }
        }
    }

    //TODO: make reload better
    override fun onResume() {
        super.onResume()
        getAddresses()
    }

    private fun getAddresses() {
        lifecycleScope.launch {
            viewModel.getAddresses().collect {
                adapter.submitData(it)
                return@collect
            }
        }
    }

    private fun setUpAdapter() {
        adapter = AddressListAdapter()
        adapter.onItemClick = {
            val action= AddressListFragmentDirections.actionNavAddressListToSelectedAddressFragment(it.id)
            navigate(action)
        }
    }

    override fun setUpRecyclers() {
        binding.addressRecycler.adapter = adapter
        binding.addressRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.addressRecycler.addItemDecoration(SpacesItemDecoration(toDp(16), true, 1))
    }

    override fun setUpOnClickListeners() {
        binding.addAddress.setOnClickListener {
            val action=AddressListFragmentDirections.actionNavAddressListToNavAddAddress(null,null,null,null)
            navigate(action)
        }
    }
}