package uz.usoft.a24seven.ui.checkout

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.databinding.FragmentCheckOutBinding
import uz.usoft.a24seven.network.models.Address
import uz.usoft.a24seven.ui.profile.myAddresses.AddressListDialogFragment
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.observeEvent
import uz.usoft.a24seven.utils.showErrorIfNotFilled


class CheckOutFragment : BaseFragment<FragmentCheckOutBinding>(FragmentCheckOutBinding::inflate) {


    private val safeArgs: CheckOutFragmentArgs by navArgs()
    private val viewModel: CheckoutViewModel by viewModel()
    private var addressId: Int =-1

    override fun setUpUI() {
        super.setUpUI()

//        setUpSpinnerAdapter()

        (requireActivity() as MainActivity).hideBottomNavigation()

        binding.totalPrice.text=getString(R.string.money_format_sum,safeArgs.checkOutData.total)
        binding.deliveryPrice.text=getString(R.string.money_format_sum,safeArgs.checkOutData.delivery_fee)
        binding.orderPrice.text=getString(R.string.money_format_sum,safeArgs.checkOutData.order_price)


        binding.checkoutPhone.setText(getString(R.string.phone_format,PrefManager.getPhone(requireContext())))

        when(PrefManager.getPaymentMethod(requireContext())) {
            "PayMe"->{binding.payme.isChecked=true}
            "Click"->{binding.click.isChecked=true}
            "Cash"->{binding.cash.isChecked=true}
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
        findNavController().navigate(R.id.action_nav_checkOut_to_nav_myOrders)
    }

    override fun setUpOnClickListeners() {

        binding.checkout.setOnClickListener {
            if(binding.radioGroup.checkedRadioButtonId!=-1) {
                val paymentMethod=when(binding.radioGroup.checkedRadioButtonId) {
                    R.id.payme->"payme"
                    R.id.click->"click"
                    R.id.cash->"cash"
                    else->"cash"
                }
                if(isAddressValid())
                    viewModel.checkout(paymentMethod,addressId,safeArgs.checkOutData.productList)
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
            addressListDialogFragment.isCancelable = false
        }
    }

    private fun isAddressValid(): Boolean {
        return if (addressId!=-1){
            true
        } else (binding.checkoutAddress.showErrorIfNotFilled() && binding.checkoutCity.showErrorIfNotFilled() && binding.checkoutDistrict.showErrorIfNotFilled())

    }

    private fun setAddress(address: Address) {
                    binding.checkoutAddress.setText(address.address)
                    binding.checkoutCity.setText(address.city)
                    binding.checkoutDistrict.setText(address.region)
    }
}