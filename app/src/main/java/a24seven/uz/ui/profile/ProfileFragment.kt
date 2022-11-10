package a24seven.uz.ui.profile

import a24seven.uz.R
import a24seven.uz.data.PrefManager
import a24seven.uz.databinding.ChangeLanguageBottomsheetBinding
import a24seven.uz.databinding.FragmentProfileBinding
import a24seven.uz.databinding.SortBottomsheetBinding
import a24seven.uz.network.models.ProfileResponse
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.network.utils.Resource
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.createBottomSheet
import a24seven.uz.utils.navigate
import a24seven.uz.utils.observeEvent
import a24seven.uz.utils.showSnackbar
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


// TODO: myOrders
// TODO: myProfileImage?

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModel()
    private var userData: ProfileResponse? = null

    private lateinit var bottomsheet: BottomSheetDialog
    private var _bottomSheetBinding: ChangeLanguageBottomsheetBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!

    override fun setUpUI() {
        super.setUpUI()
        _bottomSheetBinding = ChangeLanguageBottomsheetBinding.inflate(layoutInflater)
        bottomsheet =
            createBottomSheet(bottomSheetBinding.root)

        setTitle(getString(R.string.profile_title))
        viewModel.getProfileResponse()
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)
        data as ProfileResponse
        userData = data
        parseData(data)

    }

    private fun parseData(data: ProfileResponse) {
//        binding.userPhone.text = getString(R.string.phone_format, data.phone)
//        binding.userName.text = getString(R.string.full_name_format, data.firstName, data.lastName)
//        binding.orderCount.text = getString(R.string.order_count, data.orders_count)
//        binding.favCount.text = getString(R.string.items_count, data.favorites_count)
//        binding.addressCount.text = getString(R.string.address_count, data.addresses_count)
//        binding.payMethod.text = PrefManager.getPaymentMethod(requireContext())

        PrefManager.savePhone(requireContext(), data.phone)
        PrefManager.saveName(
            requireContext(), getString(R.string.full_name_format, data.firstName, data.lastName)
        )
    }

    override fun onResume() {
        super.onResume()
        if (userData != null) parseData(userData!!)
        mainActivity.showBottomNavigation()
    }


    override fun setUpObservers() {
        observeEvent(viewModel.profileResponse, ::handle)

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
                        findNavController().popBackStack(R.id.nav_home, true)
                        navigate(R.id.nav_home)
                    }

                    is Resource.GenericError -> {
                        hideLoadingDialog()
                        hideNoConnectionDialog()
                        showSnackbar(
                            resource.errorResponse.jsonResponse.optString("error") ?: "error"
                        )
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
        viewModel.getProfileResponse()
    }

    override fun setUpOnClickListeners() {
        binding.logout.setOnClickListener {
            viewModel.getLogoutResponse()
        }

        binding.changeLanguage

        binding.myOrders.setOnClickListener {
            navigate(R.id.action_nav_profile_to_nav_myOrders)
        }
        binding.changeLanguage.setOnClickListener {
           bottomSheet()
        }
        binding.myAdresses.setOnClickListener {
            val action = ProfileFragmentDirections.actionNavProfileToNavAddressList()
            navigate(action)
        }
        binding.myPaymentMethod.setOnClickListener {
            navigate(R.id.action_nav_profile_to_nav_myPaymentMethod)
        }

        binding.profileSettings.setOnClickListener {
            val action = ProfileFragmentDirections.actionNavProfileToNavProfileSettings()
            navigate(action)
        }

        binding.myFavorites.setOnClickListener {
            navigate(R.id.action_nav_profile_to_nav_myFavouriteItems)
        }
    }

    lateinit var local:String

    fun bottomSheet(){

        bottomsheet.show()


        _bottomSheetBinding?.ruButton?.setOnClickListener {
            local = "ru"
            _bottomSheetBinding?.ruButton?.setBackgroundResource(R.drawable.shape_language)
            _bottomSheetBinding?.uzButton?.setBackgroundResource(R.drawable.shape_language1)
//            changeLocale("ru")
        }

        _bottomSheetBinding?.uzButton?.setOnClickListener {
            local = "uz"
            _bottomSheetBinding?.uzButton?.setBackgroundResource(R.drawable.shape_language)
            _bottomSheetBinding?.ruButton?.setBackgroundResource(R.drawable.shape_language1)
//            changeLocale("uz")
        }

        _bottomSheetBinding?.confirmation?.setOnClickListener {
            changeLocale(local)
            bottomsheet.dismiss()
        }

    }

    private fun changeLocale(locale: String) {
        val oldLocale = PrefManager.getLocale(requireContext())
        if (oldLocale != locale) {
            PrefManager.saveLocale(requireContext(), locale.toLowerCase(Locale.ROOT))
            ActivityCompat.recreate(requireActivity())
        }
    }


}