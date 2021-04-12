package uz.usoft.a24seven.ui.profile.myAddresses

import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.databinding.FragmentAddAddressBinding
import uz.usoft.a24seven.ui.profile.ProfileViewModel
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.observeEvent
import uz.usoft.a24seven.utils.showErrorIfNotFilled
import uz.usoft.a24seven.utils.showSnackbar

class AddAddressFragment : BaseFragment<FragmentAddAddressBinding>(FragmentAddAddressBinding::inflate) {

    private var lat=46.00
    private var lng=69.00



    private val viewModel: ProfileViewModel by viewModel()

    override fun setUpOnClickListeners() {
        binding.addAddress.setOnClickListener {
            val addressName=binding.addAddressName.text.toString()
            val addressAddress=binding.addAddressAddress.text.toString()
            val addressCity=binding.addAddressCity.text.toString()
            val addressRegion=binding.addAddressDistrict.text.toString()
            if(isValid())
                viewModel.addAddress(addressName,addressAddress,addressCity,addressRegion,lat,lng,PrefManager.getPhone(requireContext()))
        }
    }


    override fun onRetry() {
        super.onRetry()
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        showSnackbar("Success")
        findNavController().popBackStack()
    }

    override fun setUpObservers() {
        observeEvent(viewModel.addAddressResponse,::handle)
    }

    fun isValid():Boolean{
        return (binding.addAddressName.showErrorIfNotFilled() &&
                binding.addAddressAddress.showErrorIfNotFilled() &&
                binding.addAddressCity.showErrorIfNotFilled() &&
                binding.addAddressDistrict.showErrorIfNotFilled())
    }
}