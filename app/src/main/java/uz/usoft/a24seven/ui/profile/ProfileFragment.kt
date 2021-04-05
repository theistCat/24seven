package uz.usoft.a24seven.ui.profile

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.databinding.FragmentProfileBinding
import uz.usoft.a24seven.network.models.ProfileResponse
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.observeEvent
import uz.usoft.a24seven.utils.showSnackbar


class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel:ProfileViewModel by viewModel()
    private var userData: ProfileResponse?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getProfileResponse()
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        data as ProfileResponse
        userData=data
        parseData(data)

    }

    private fun parseData(data:ProfileResponse)
    {
        binding.userPhone.text=getString(R.string.phone_format, data.phone)
        binding.userName.text=getString(R.string.full_name_format, data.firstName, data.lastName)
        binding.orderCount.text=getString(R.string.order_count, data.orders_count)
        binding.favCount.text=getString(R.string.items_count, data.favorites_count)
        binding.addressCount.text=getString(R.string.address_count, data.addresses_count)
    }

    override fun onResume() {
        super.onResume()
        if(userData!=null) parseData(userData!!)
    }


    override fun setUpObservers() {
        observeEvent(viewModel.profileResponse,::handle)

        viewModel.logoutResponse.observe(this, Observer {
            it.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showLoadingDialog()
                        hideNoConnectionDialog()
                    }
                    is Resource.Success -> {
                        hideLoadingDialog()
                        hideNoConnectionDialog()
                        PrefManager.saveToken(requireContext(), "")
                        findNavController().popBackStack(R.id.nav_home, false)
                    }
                    is Resource.GenericError -> {
                        hideLoadingDialog()
                        hideNoConnectionDialog()
                        showSnackbar(resource.errorResponse.jsonResponse.getString("error"))
                    }
                    is Resource.Error -> {
                        hideLoadingDialog()
                        if (resource.exception is NoConnectivityException) {
                            showNoConnectionDialog(this::onRetry)
                        } else resource.exception.message?.let { it1 -> showSnackbar(it1) }
                    }
                }
            }
        })
    }

    override fun onRetry() {
        hideNoConnectionDialog()
    }

    override fun setUpOnClickListeners()
    {
        binding.logout.setOnClickListener {
            viewModel.getLogoutResponse()
        }
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
            val action= ProfileFragmentDirections.actionNavProfileToNavProfileSettings()
            navigate(action)
        }

        binding.myFavouriteItems.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_nav_myFavouriteItems)
        }
    }


}