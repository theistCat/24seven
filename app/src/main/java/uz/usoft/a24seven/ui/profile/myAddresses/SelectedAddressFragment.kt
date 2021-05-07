package uz.usoft.a24seven.ui.profile.myAddresses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.databinding.FragmentSelectedAddressBinding
import uz.usoft.a24seven.network.models.Address
import uz.usoft.a24seven.network.utils.Variables
import uz.usoft.a24seven.ui.category.selectedSubCategory.SelectedSubCategoryFragmentDirections
import uz.usoft.a24seven.ui.profile.ProfileViewModel
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.observeEvent
import uz.usoft.a24seven.utils.showErrorIfNotFilled
import uz.usoft.a24seven.utils.showSnackbar

class SelectedAddressFragment : BaseFragment<FragmentSelectedAddressBinding>(FragmentSelectedAddressBinding::inflate) {


    private var lat=0.00
    private var lng=0.00


    private val viewModel: ProfileViewModel by viewModel()
    private val safeArgs: SelectedAddressFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        viewModel.showAddress(safeArgs.addressId)
    }

    override fun onRetry() {
        super.onRetry()
        viewModel.showAddress(safeArgs.addressId)
    }

    override fun setUpObservers() {
        observeEvent(viewModel.showAddressResponse,::handle)
        observeEvent(viewModel.updateAddressResponse,::handle)
        observeEvent(viewModel.deleteAddressResponse,::handle)
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        if(data is Address) {
            binding.selectedAddressName.setText(data.name)
            binding.selectedAddressAddress.setText(data.address)
            binding.selectedAddressCity.setText(data.city)
            binding.selectedAddressDistrict.setText(data.region)
            if(data.location!=null) {
                lat = data.location.lat.toDouble()
                lng = data.location.lng.toDouble()
            }


            if(safeArgs.address!=null)
                binding.selectedAddressAddress.setText(safeArgs.address)
            if(safeArgs.region!=null)
                binding.selectedAddressDistrict.setText(safeArgs.region)
            if(safeArgs.city!=null)
                binding.selectedAddressCity.setText(safeArgs.city)
            if(safeArgs.point!=null)
            {
                lat=safeArgs.point!!.lat.toDouble()
                lng=safeArgs.point!!.lng.toDouble()
            }
        }
        else {
            showSnackbar(getString(R.string.success))
            findNavController().popBackStack()
        }
    }

    override fun setUpOnClickListeners() {
        //TODO: make this button disabled if no changes were made
        binding.saveAddress.setOnClickListener {
            val addressName=binding.selectedAddressName.text.toString()
            val addressAddress=binding.selectedAddressAddress.text.toString()
            val addressCity=binding.selectedAddressCity.text.toString()
            val addressRegion=binding.selectedAddressDistrict.text.toString()
            if(isValid())
                viewModel.updateAddress(safeArgs.addressId,addressName,addressAddress,addressCity,addressRegion,lat,lng,PrefManager.getPhone(requireContext()))
        }
        binding.delete.setOnClickListener {
                viewModel.deleteAddress(safeArgs.addressId)
        }

        binding.showOnMap.setOnClickListener {
            val action=SelectedAddressFragmentDirections.actionNavSelectedAddressToNavMap(null,Variables.fromEditAddress,addressId = safeArgs.addressId)
            navigate(action)
        }
    }

    fun isValid():Boolean{
        return (binding.selectedAddressName.showErrorIfNotFilled() &&
                binding.selectedAddressAddress.showErrorIfNotFilled() &&
                binding.selectedAddressCity.showErrorIfNotFilled() &&
                binding.selectedAddressDistrict.showErrorIfNotFilled())
    }
}