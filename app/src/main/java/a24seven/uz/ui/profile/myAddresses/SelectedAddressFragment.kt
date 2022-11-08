package a24seven.uz.ui.profile.myAddresses

import android.os.Bundle
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import a24seven.uz.R
import a24seven.uz.data.PrefManager
import a24seven.uz.databinding.FragmentSelectedAddressBinding
import a24seven.uz.network.models.Address
import a24seven.uz.network.models.Region
import a24seven.uz.network.utils.Variables
import a24seven.uz.ui.profile.ProfileViewModel
import a24seven.uz.ui.profile.TypedDropdownAdapter
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.navigate
import a24seven.uz.utils.observeEvent
import a24seven.uz.utils.showErrorIfNotFilled
import a24seven.uz.utils.showSnackbar

class SelectedAddressFragment : BaseFragment<FragmentSelectedAddressBinding>(FragmentSelectedAddressBinding::inflate) {


    private var lat=0.00
    private var lng=0.00

    private var regionId=-1
    private var cityId=-1

    val regionsArray=ArrayList<Region>()
    val citiesArray=ArrayList<Region>()

    private val viewModel: ProfileViewModel by viewModel()
    private val safeArgs: SelectedAddressFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        viewModel.getRegions()
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
        observeEvent(viewModel.regionsResponse,::handle)
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)

        when(data){
            is Address ->{
                binding.selectedAddressName.setText(data.name)
                binding.selectedAddressAddress.setText(data.address)
                binding.selectedAddressCity.setText(data.city)
                binding.selectedAddressDistrict.setText(data.region)
                if(data.location!=null) {
                    lat = data.location.lat.toDouble()
                    lng = data.location.lng.toDouble()
                }


                regionId=data.region_id?:-1
                binding.regionDropDownValue.let {
                    it.setText((it.adapter as TypedDropdownAdapter).setSelection(regionId),false)
                    citiesArray.clear()
                    citiesArray.addAll((it.adapter as TypedDropdownAdapter).getSelection(regionId)?.cities?: arrayListOf() )
                }

                cityId=data.city_id?:-1
                binding.cityDropDownValue.let {
                    it.setText((it.adapter as TypedDropdownAdapter).setSelection(cityId),false)
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

                safeArgs.addAddressData?.let {
                    with(binding){
                        this.selectedAddressName.setText(it.name)
                        this.regionDropDownValue.setText(it.region,false)
                        this.cityDropDownValue.setText(it.city,false)
                        regionId=it.regionId
                        cityId=it.cityId
                    }
                }
            }
            is List<*>->{
                regionsArray.addAll(data as ArrayList<Region>)
            }
            else->{
                showSnackbar(getString(R.string.success))
                findNavController().popBackStack()
            }
        }

    }

    override fun setUpUI() {
        super.setUpUI()

        val arrayAdapter = TypedDropdownAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item,
            regionsArray)

        (binding.regionDropDownValue as? AutoCompleteTextView)?.setAdapter(arrayAdapter)


        binding.regionDropDownValue.setOnItemClickListener { adapterView, view, i, l ->
            arrayAdapter.getItem(i)?.let {
                regionId=it.id
                citiesArray.clear()
                citiesArray.addAll(it.cities as ArrayList<Region>)

            }
        }

        val cityAdapter = TypedDropdownAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item,
            citiesArray)

        (binding.cityDropDownValue as? AutoCompleteTextView)?.setAdapter(cityAdapter)


        binding.cityDropDownValue.setOnItemClickListener { adapterView, view, i, l ->
            cityAdapter.getItem(i)?.let {
                cityId=it.id
            }
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
                viewModel.updateAddress(safeArgs.addressId,addressName,addressAddress,addressCity,addressRegion,lat,lng,
                    PrefManager.getPhone(requireContext())
                ,regionId, cityId)
        }
        binding.delete.setOnClickListener {
                viewModel.deleteAddress(safeArgs.addressId)
        }

        binding.showOnMap.setOnClickListener {
            val action=SelectedAddressFragmentDirections.actionNavSelectedAddressToNavMap(null,
                Variables.fromEditAddress,addressId = safeArgs.addressId,
            addAddressData =
                AddAddressData(
                    binding.selectedAddressName.text.toString(),
                    binding.regionDropDownValue.text.toString(),
                    regionId,
                    binding.cityDropDownValue.text.toString(),
                    cityId
                )
            )
            navigate(action)
        }
    }

    fun isValid():Boolean{
        return (binding.selectedAddressName.showErrorIfNotFilled() &&
                binding.selectedAddressAddress.showErrorIfNotFilled() &&
                binding.selectedAddressCity.showErrorIfNotFilled() &&
                binding.selectedAddressDistrict.showErrorIfNotFilled()&&
                regionId!=-1 &&
                cityId!=-1)
    }
}