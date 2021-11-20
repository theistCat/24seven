package uz.usoft.a24seven.ui.checkout

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.databinding.FragmentCheckOutBinding
import uz.usoft.a24seven.network.models.Address
import uz.usoft.a24seven.network.models.Region
import uz.usoft.a24seven.network.utils.Variables
import uz.usoft.a24seven.ui.profile.TypedDropdownAdapter
import uz.usoft.a24seven.ui.profile.myAddresses.AddAddressData
import uz.usoft.a24seven.ui.profile.myAddresses.AddressListDialogFragment
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.observeEvent
import uz.usoft.a24seven.utils.showErrorIfNotFilled
import uz.usoft.a24seven.utils.showSnackbar


class CheckOutFragment : BaseFragment<FragmentCheckOutBinding>(FragmentCheckOutBinding::inflate) {


    private val safeArgs: CheckOutFragmentArgs by navArgs()
    private val viewModel: CheckoutViewModel by viewModel()

    private var addressId: Int =-1
    private var lat="0.0"
    private var lng="0.0"
    private val address=HashMap<String,String>()

    val regionsArray=ArrayList<Region>()
    val citiesArray=ArrayList<Region>()

    private var regionId=-1
    private var cityId=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getRegions()
    }

    override fun setUpUI() {
        super.setUpUI()

//        setUpSpinnerAdapter()


        if(safeArgs.address!=null) {
            binding.checkoutAddress.setText(safeArgs.address)
            binding.checkoutAddress.visibility= View.VISIBLE
        }
        if(safeArgs.region!=null) {
            binding.checkoutDistrict.setText(safeArgs.region)
            binding.checkoutDistrict.visibility= View.VISIBLE
        }
        if(safeArgs.city!=null) {
            binding.checkoutCity.setText(safeArgs.city)
            binding.checkoutCity.visibility= View.VISIBLE
        }

        if(safeArgs.point!=null)
        {
            lat=safeArgs.point!!.lat
            lng=safeArgs.point!!.lng
        }

        safeArgs.addAddressData?.let {
            with(binding){
                this.regionDropDownValue.setText(it.region,false)
                this.cityDropDownValue.setText(it.city,false)
                regionId=it.regionId
                cityId=it.cityId
            }
        }

        binding.totalPrice.text=getString(R.string.money_format_sum,safeArgs.checkOutData.total)
        binding.deliveryPrice.text=getString(R.string.money_format_sum,safeArgs.checkOutData.delivery_fee)
        binding.orderPrice.text=getString(R.string.money_format_sum,safeArgs.checkOutData.order_price)


        binding.checkoutPhone.setText(getString(R.string.phone_format,PrefManager.getPhone(requireContext())))

        when(PrefManager.getPaymentMethod(requireContext())) {
            getString(R.string.cash)->{binding.cash.isChecked=true}
            getString(R.string.transfer)->{binding.transfer.isChecked=true}
            else-> {
                binding.cash.isChecked=true
            }
        }

        val arrayAdapter = TypedDropdownAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item,
            regionsArray)

        (binding.regionDropDownValue as? AutoCompleteTextView)?.setAdapter(arrayAdapter)


        binding.regionDropDownValue.setOnItemClickListener { adapterView, view, i, l ->
            binding.regionDropDown.error=null
            arrayAdapter.getItem(i)?.let {
                regionId=it.id
                citiesArray.clear()
                citiesArray.addAll(it.cities as ArrayList<Region>)

            }
        }

        val cityAdapter = TypedDropdownAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item,
            citiesArray)

        (binding.cityDropDownValue as? AutoCompleteTextView)?.setAdapter(cityAdapter)


        binding.cityDropDownValue.setOnClickListener {
            if(cityAdapter.isEmpty)
                Toast.makeText(requireContext(), getString(R.string.warning_region), Toast.LENGTH_SHORT).show()
        }
        binding.cityDropDownValue.setOnItemClickListener { adapterView, view, i, l ->

            cityAdapter.getItem(i)?.let {
                cityId=it.id
            }
        }
    }


//    private fun setUpSpinnerAdapter() {
//        val spinner = binding.savedAddresses
//        ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.savedAddr,
//            R.layout.spinner_lay
//        ).also { adapter ->
//            // Specify the layout to use when the list of choices appears
//            adapter.setDropDownViewResource(R.layout.spinner_dropdown_lay)
//            // Apply the adapter to the spinner
//            spinner.adapter = adapter
//        }
//    }

    override fun setUpObservers() {
        observeEvent(viewModel.checkoutResponse,::handle)
        observeEvent(viewModel.regionsResponse,::handle)
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        when(data)
        {
            is List<*>->{
                regionsArray.addAll(data as ArrayList<Region>)
            }
            else->{
                viewModel.emptyTheCart()
                var i=0
                safeArgs.checkOutData.productList.forEach{
                    Log.d("cart",it.key)
                    if(it.key.matches(Regex("(products\\[[0-9]\\]+\\[id\\])")))
                        PrefManager.getInstance(requireContext()).edit().remove(it.value.toString()).apply()
                    i++
                }
                findNavController().navigate(R.id.action_nav_checkOut_to_nav_myOrders)
            }
        }

    }

    override fun setUpOnClickListeners() {

        binding.checkout.setOnClickListener {
            if(binding.radioGroup.checkedRadioButtonId!=-1) {
                val paymentMethod=when(binding.radioGroup.checkedRadioButtonId) {
                    R.id.transfer->"transfer"
                    R.id.cash->"cash"
                    else->"cash"
                }
                if(isAddressValid()) {
                    if(binding.regionDropDownValue.showErrorIfNotFilled(binding.regionDropDown)
                        && binding.cityDropDownValue.showErrorIfNotFilled(binding.cityDropDown)) {
                        if (addressId != -1)
                            viewModel.checkout(
                                paymentMethod,
                                addressId,
                                safeArgs.checkOutData.productList
                            )
                        else {
                            address["address[name]"] = "Manual"
                            address["address[phone]"] = binding.checkoutPhone.text.toString()
                            address["address[region]"] = binding.checkoutDistrict.text.toString()
                            address["address[city]"] = binding.checkoutCity.text.toString()
                            address["address[location][lat]"] = lat
                            address["address[location][lng]"] = lng
                            address["address[address]"] = binding.checkoutAddress.text.toString()
                            address["address[region_id]"] = regionId.toString()
                            address["address[city_id]"] = cityId.toString()
                            viewModel.checkout(
                                paymentMethod,
                                null,
                                safeArgs.checkOutData.productList,
                                address
                            )
                        }
                    }
                    else binding.scrollView.smoothScrollTo(binding.regionDropDownValue.scrollX,binding.regionDropDownValue.scrollY)
                }
                else showSnackbar(getString(R.string.chose_address))
            }
        }

        binding.savedAddresses.setOnClickListener {
            val fm = requireActivity().supportFragmentManager
            val addressListDialogFragment = AddressListDialogFragment()
            addressListDialogFragment.onItemClick={
                setAddress(it)
                addressId=it.id
                addressListDialogFragment.dismiss()
            }
            addressListDialogFragment.show(fm, "AddressList")
            addressListDialogFragment.isCancelable = true
        }

        binding.fromMap.setOnClickListener {
            val action=CheckOutFragmentDirections.actionNavCheckOutToNavMap(safeArgs.checkOutData,
                Variables.fromCheckout,addressId = -1, addAddressData = AddAddressData(
                    "Manual",
                    binding.regionDropDownValue.text.toString(),
                    regionId,
                    binding.cityDropDownValue.text.toString(),
                    cityId
                )
            )
            navigate(action)
        }
    }

    private fun isAddressValid(): Boolean {
        return (binding.checkoutAddress.showErrorIfNotFilled()
                && binding.checkoutCity.showErrorIfNotFilled()
                && binding.checkoutDistrict.showErrorIfNotFilled())

    }

    private fun setAddress(address: Address) {
                    binding.checkoutAddress.setText(address.address)

                    binding.checkoutAddress.visibility= View.VISIBLE
                    binding.checkoutCity.setText(address.city)
                    binding.checkoutCity.visibility= View.VISIBLE
                    binding.checkoutDistrict.setText(address.region)
                    binding.checkoutDistrict.visibility= View.VISIBLE

                    regionId=address.region_id?:-1
                    binding.regionDropDownValue.let {
                        it.setText((it.adapter as TypedDropdownAdapter).setSelection(regionId),false)
                        citiesArray.clear()
                        citiesArray.addAll((it.adapter as TypedDropdownAdapter).getSelection(regionId)?.cities?: arrayListOf() )
                    }

                    cityId=address.city_id?:-1
                    binding.cityDropDownValue.let {
                        it.setText((it.adapter as TypedDropdownAdapter).setSelection(cityId),false)
                    }
    }

    override fun onResume() {
        super.onResume()

        (requireActivity() as MainActivity).hideBottomNavigation()
    }
}