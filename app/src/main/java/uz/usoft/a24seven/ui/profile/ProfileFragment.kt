package uz.usoft.a24seven.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.databinding.FragmentProfileBinding
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.hideProgress
import uz.usoft.a24seven.utils.observeEvent
import uz.usoft.a24seven.utils.showAsProgress
import uz.usoft.a24seven.utils.showSnackbar

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel:ProfileViewModel by viewModel()

    override fun setUpObservers() {
        viewModel.logoutResponse.observe(this, Observer {
            it.getContentIfNotHandled()?.let {resource->
                when (resource) {
                    is Resource.Loading -> {
                        showLoadingDialog()
                        hideNoConnectionDialog()
                    }
                    is Resource.Success -> {
                        hideLoadingDialog()
                        hideNoConnectionDialog()
                        PrefManager.saveToken(requireContext(),"")
                        mainActivity.recreate()
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
            findNavController().navigate(R.id.action_nav_profile_to_nav_profileSettings)
        }

        binding.myFavouriteItems.setOnClickListener {
            findNavController().navigate(R.id.action_nav_profile_to_nav_myFavouriteItems)
        }
    }

}