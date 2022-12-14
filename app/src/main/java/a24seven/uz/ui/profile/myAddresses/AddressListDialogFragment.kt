package a24seven.uz.ui.profile.myAddresses

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import a24seven.uz.R
import a24seven.uz.databinding.FragmentAddressListDialogBinding
import a24seven.uz.network.models.Address
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.ui.profile.ProfileViewModel
import a24seven.uz.utils.SpacesItemDecoration
import a24seven.uz.utils.showSnackbar
import a24seven.uz.utils.toDp

class AddressListDialogFragment : DialogFragment() {

    private var _binding:FragmentAddressListDialogBinding?=null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModel()
    private lateinit var adapter: AddressListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpAdapter()
        getAddresses()
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(),R.color.transparent)));
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentAddressListDialogBinding.inflate(layoutInflater,container,false)
        setUpRecyclers()
        setUpObservers()
        return binding.root
    }

    private fun getAddresses() {
        lifecycleScope.launch {
            viewModel.getAddresses().collect {
                adapter.submitData(it)
                return@collect
            }
        }
    }

    var onItemClick: ((Address) -> Unit)? = null

    private fun setUpAdapter() {
        adapter = AddressListAdapter()
        adapter.onItemClick = {
            onItemClick?.invoke(it)
        }
    }

    fun setUpRecyclers() {
        binding.addAddressList.adapter = adapter
        binding.addAddressList.layoutManager = LinearLayoutManager(requireContext())
        binding.addAddressList.addItemDecoration(SpacesItemDecoration(toDp(16), true, 1))
    }

    fun setUpObservers() {
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
                            showSnackbar(getString(R.string.loading_error))
                        }
                        else{
                            showSnackbar(error.error.message.toString())
                        }
                    }
                    is LoadState.Loading->{

//                        showSnackbar("Loading...")
                    }
                    else->{
                        if(adapter.itemCount==0) {
                            Toast.makeText(requireContext(),getString(R.string.no_addresses),Toast.LENGTH_SHORT).show()
                            this@AddressListDialogFragment.dismiss()
                        }
                    }
                }

            }
        }
    }

}