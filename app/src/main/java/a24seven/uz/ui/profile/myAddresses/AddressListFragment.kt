package a24seven.uz.ui.profile.myAddresses

import a24seven.uz.R
import a24seven.uz.databinding.FragmentAddressListBinding
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.ui.profile.ProfileViewModel
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.SpacesItemDecoration
import a24seven.uz.utils.navigate
import a24seven.uz.utils.showSnackbar
import a24seven.uz.utils.toDp
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddressListFragment :
    BaseFragment<FragmentAddressListBinding>(FragmentAddressListBinding::inflate) {


    private val viewModel: ProfileViewModel by viewModel()
    private lateinit var adapter: AddressListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        setUpAdapter()
        getAddresses()
        // on below line we are creating a new bottom sheet dialog.
//        val dialog = BottomSheetDialog(requireContext())
//
//        // on below line we are inflating a layout file which we have created.
//        val view = layoutInflater.inflate(R.layout.fragment_address_list, null)
//
//        // on below line we are creating a variable for our button
//        // which we are using to dismiss our dialog.
////        val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)
////
////        // on below line we are adding on click listener
////        // for our dismissing the dialog button.
////        btnClose.setOnClickListener {
////            // on below line we are calling a dismiss
////            // method to close our dialog.
////            dialog.dismiss()
////        }
//        // below line is use to set cancelable to avoid
//        // closing of dialog box when clicking on the screen.
//        dialog.setCancelable(true)
//
//        // on below line we are setting
//        // content view to our view.
//        dialog.setContentView(view)
//
//        // on below line we are calling
//        // a show method to display a dialog.
//        dialog.show()

    }


    override fun setUpObservers() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                //progressBar.isVisible = loadStates.refresh is LoadState.Loading
                //retry.isVisible = loadState.refresh !is LoadState.Loading
                when (loadStates.refresh) {
                    is LoadState.Error -> {
                        val error = loadStates.refresh as LoadState.Error

                        hideLoadingDialog()
                        if (error.error is NoConnectivityException) {
                            showNoConnectionDialog(this@AddressListFragment::onRetry)
                        } else {
                            showSnackbar(error.error.message.toString())
                        }
                    }

                    is LoadState.Loading -> {
                        hideNoConnectionDialog()
                        showLoadingDialog()
                    }

                    else -> {
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
            val action =
                AddressListFragmentDirections.actionNavAddressListToSelectedAddressFragment(
                    it.id,
                    null,
                    null,
                    null,
                    null
                )
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
            val action = AddressListFragmentDirections.actionNavAddressListToNavAddAddress(
                null,
                null,
                null,
                null
            )
            navigate(action)
        }
    }
}