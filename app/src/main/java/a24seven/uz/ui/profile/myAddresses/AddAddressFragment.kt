package a24seven.uz.ui.profile.myAddresses

import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import a24seven.uz.R
import a24seven.uz.data.PrefManager
import a24seven.uz.databinding.FragmentAddAddressBinding
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
import java.io.Serializable

class AddAddressFragment : BaseFragment<FragmentAddAddressBinding>(FragmentAddAddressBinding::inflate) {

    private var lat=00.00
    private var lng=00.00
    private var regionId=-1
    private var cityId=-1

    val regionsArray=ArrayList<Region>()
    val citiesArray=ArrayList<Region>()

    private val safeArgs: AddAddressFragmentArgs by navArgs()
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getRegions()
    }

    override fun setUpOnClickListeners() {
        binding.addAddress.setOnClickListener {
            val addressName=binding.addAddressName.text.toString()
            val addressAddress=binding.addAddressAddress.text.toString()
            val addressCity=binding.addAddressCity.text.toString()
            val addressRegion=binding.addAddressDistrict.text.toString()
            if(isValid()) {
                binding.regionDropDown.error=null
                    binding.cityDropDown.error=null
                viewModel.addAddress(
                    addressName,
                    addressAddress,
                    addressCity,
                    addressRegion,
                    lat,
                    lng,
                    PrefManager.getPhone(requireContext()),
                    regionId,
                    cityId
                )
            }else{
                if (regionId==-1)
                    binding.regionDropDown.error="error"
                if (cityId==-1)
                    binding.cityDropDown.error="error"
            }
        }

        binding.showOnMap.setOnClickListener {
                val action=
                    AddAddressFragmentDirections.actionNavAddAddressToNavMap(null,
                        Variables.fromAddAddress,
                        addressId = -1,
                    AddAddressData(
                        binding.addAddressName.text.toString(),
                        binding.regionDropDownValue.text.toString(),
                        regionId,
                        binding.cityDropDownValue.text.toString(),
                        cityId
                    )
                    )
                navigate(action)
        }
    }

    override fun setUpUI() {
        super.setUpUI()


        if(safeArgs.address!=null) {
            binding.addAddressAddress.setText(safeArgs.address)
            binding.addAddressAddress.visibility= View.VISIBLE
        }
        if(safeArgs.region!=null) {
            binding.addAddressDistrict.setText(safeArgs.region)
            binding.addAddressDistrict.visibility = View.VISIBLE
        }
        if(safeArgs.city!=null) {
            binding.addAddressCity.setText(safeArgs.city)
            binding.addAddressCity.visibility= View.VISIBLE
        }
        if(safeArgs.point!=null)
        {
            lat=safeArgs.point!!.lat.toDouble()
            lng=safeArgs.point!!.lng.toDouble()
        }

        safeArgs.addAddressData?.let {
            with(binding){
                this.addAddressName.setText(it.name)
                this.regionDropDownValue.setText(it.region,false)
                this.cityDropDownValue.setText(it.city,false)
                regionId=it.regionId
                cityId=it.cityId
            }
        }

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

    override fun onRetry() {
        super.onRetry()
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        when(data){
            is Address ->{
                showSnackbar(getString(R.string.success))
                findNavController().popBackStack(R.id.nav_addressList,false)

            }
            is List<*>->{
                regionsArray.addAll(data as ArrayList<Region>)
            }
        }
       }

    override fun setUpObservers() {
        observeEvent(viewModel.addAddressResponse,::handle)
        observeEvent(viewModel.regionsResponse,::handle)
    }

    fun isValid():Boolean{
        return (binding.addAddressName.showErrorIfNotFilled() &&
                binding.addAddressAddress.showErrorIfNotFilled() &&
                binding.addAddressCity.showErrorIfNotFilled() &&
                binding.addAddressDistrict.showErrorIfNotFilled() &&
                regionId!=-1 &&
                cityId!=-1)
    }
}

class AddAddressData(
    val name:String,
    val region:String,
    val regionId:Int,
    val city:String,
    val cityId:Int
): Serializable