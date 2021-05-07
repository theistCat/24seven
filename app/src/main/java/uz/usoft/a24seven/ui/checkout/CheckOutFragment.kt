package uz.usoft.a24seven.ui.checkout

import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.databinding.FragmentCheckOutBinding
import uz.usoft.a24seven.network.models.Address
import uz.usoft.a24seven.network.utils.Variables
import uz.usoft.a24seven.ui.profile.myAddresses.AddressListDialogFragment
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.navigate
import uz.usoft.a24seven.utils.observeEvent
import uz.usoft.a24seven.utils.showErrorIfNotFilled


class CheckOutFragment : BaseFragment<FragmentCheckOutBinding>(FragmentCheckOutBinding::inflate) {


    private val safeArgs: CheckOutFragmentArgs by navArgs()
    private val viewModel: CheckoutViewModel by viewModel()
    private var addressId: Int =-1
    private var lat="0.0"
    private var lng="0.0"
    private val address=HashMap<String,String>()

    override fun setUpUI() {
        super.setUpUI()

//        setUpSpinnerAdapter()


        if(safeArgs.address!=null)
            binding.checkoutAddress.setText(safeArgs.address)
        if(safeArgs.region!=null)
            binding.checkoutDistrict.setText(safeArgs.region)
        if(safeArgs.city!=null)
            binding.checkoutCity.setText(safeArgs.city)

        if(safeArgs.point!=null)
        {
            lat=safeArgs.point!!.lat
            lng=safeArgs.point!!.lng
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
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
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

    override fun setUpOnClickListeners() {

        binding.checkout.setOnClickListener {
            if(binding.radioGroup.checkedRadioButtonId!=-1) {
                val paymentMethod=when(binding.radioGroup.checkedRadioButtonId) {
                    R.id.transfer->"transfer"
                    R.id.cash->"cash"
                    else->"cash"
                }
                if(isAddressValid())
                    if(addressId!=-1)
                        viewModel.checkout(paymentMethod,addressId,safeArgs.checkOutData.productList)
                    else {
                        address["address[name]"]="Manual"
                        address["address[phone]"]= binding.checkoutPhone.text.toString()
                        address["address[region]"]=binding.checkoutDistrict.text.toString()
                        address["address[city]"]=binding.checkoutCity.text.toString()
                        address["address[location][lat]"]=lat
                        address["address[location][lng]"]=lng
                        address["address[address]"]=binding.checkoutAddress.text.toString()
                        viewModel.checkout(
                            paymentMethod,
                            null,
                            safeArgs.checkOutData.productList,
                            address
                        )
                    }
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
                Variables.fromCheckout,addressId = -1)
            navigate(action)
        }
    }

    private fun isAddressValid(): Boolean {
        return (binding.checkoutAddress.showErrorIfNotFilled() && binding.checkoutCity.showErrorIfNotFilled() && binding.checkoutDistrict.showErrorIfNotFilled())

    }

    private fun setAddress(address: Address) {
                    binding.checkoutAddress.setText(address.address)
                    binding.checkoutCity.setText(address.city)
                    binding.checkoutDistrict.setText(address.region)
    }

    override fun onResume() {
        super.onResume()

        (requireActivity() as MainActivity).hideBottomNavigation()
    }
}