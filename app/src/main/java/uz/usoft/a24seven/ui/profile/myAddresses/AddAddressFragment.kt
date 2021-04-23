package uz.usoft.a24seven.ui.profile.myAddresses

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.databinding.FragmentAddAddressBinding
import uz.usoft.a24seven.ui.profile.ProfileViewModel
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.observeEvent
import uz.usoft.a24seven.utils.showErrorIfNotFilled
import uz.usoft.a24seven.utils.showSnackbar

class AddAddressFragment : BaseFragment<FragmentAddAddressBinding>(FragmentAddAddressBinding::inflate) {

    private var lat=00.00
    private var lng=00.00


    private val safeArgs: AddAddressFragmentArgs by navArgs()
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

        binding.showOnMap.setOnClickListener {
                val action=AddAddressFragmentDirections.actionNavAddAddressToNavMap(null)
                navigate(action)
        }
    }

    override fun setUpUI() {
        super.setUpUI()


        if(safeArgs.address!=null)
            binding.addAddressAddress.setText(safeArgs.address)
        if(safeArgs.region!=null)
            binding.addAddressDistrict.setText(safeArgs.region)
        if(safeArgs.city!=null)
            binding.addAddressCity.setText(safeArgs.city)
        if(safeArgs.point!=null)
        {
            lat=safeArgs.point!!.lat.toDouble()
            lng=safeArgs.point!!.lng.toDouble()
        }
    }

    override fun onRetry() {
        super.onRetry()
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        showSnackbar("Success")
        findNavController().popBackStack(R.id.nav_addressList,false)
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