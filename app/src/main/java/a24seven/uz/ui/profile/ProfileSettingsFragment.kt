package a24seven.uz.ui.profile

import a24seven.uz.R
import a24seven.uz.data.PrefManager
import a24seven.uz.databinding.ChangeLanguageBottomsheetBinding
import a24seven.uz.databinding.FragmentProfileSettingsBinding
import a24seven.uz.network.models.ProfileResponse
import a24seven.uz.network.models.Region
import a24seven.uz.network.utils.Resource
import a24seven.uz.ui.utils.BaseFragment
import a24seven.uz.utils.createBottomSheet
import a24seven.uz.utils.hideKeyboard
import a24seven.uz.utils.observeEvent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


//Todo: disable update button if no update are made
class ProfileSettingsFragment :
    BaseFragment<FragmentProfileSettingsBinding>(FragmentProfileSettingsBinding::inflate) {

    private val viewModel: ProfileViewModel by viewModel()
    var uiMode: Int = 0

    //    private val c: Calendar = Calendar.getInstance()
//    val year = c.get(Calendar.YEAR)
//    val month = c.get(Calendar.MONTH)
//    val day = c.get(Calendar.DAY_OF_MONTH)
    var region: Int = -1

    val regionsArray = ArrayList<Region>()

    private lateinit var bottomsheet: BottomSheetDialog
    private var _bottomSheetBinding: ChangeLanguageBottomsheetBinding? = null
    private val bottomSheetBinding get() = _bottomSheetBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getProfileResponse()
        viewModel.getRegions()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.setOnClickListener {
//           it.hideKeyboard()
//        }
        binding.profile.setOnClickListener {
            it.hideKeyboard()
        }
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)

        when (data) {
            is ProfileResponse -> {
                binding.profilePhone.setText(getString(R.string.phone_format, data.phone))

                if (data.inn != null && data.inn != 0) {
                    binding.profileINN.setText(data.inn.toString())
                }

                if (data.name != null) {
                    binding.profileStoreName.setText(data.name)
                }

                val monthName = resources.getStringArray(R.array.month)

                if (data.first_name != null && data.last_name != null)
                    binding.profileFullName.setText(
                        getString(
                            R.string.full_name_format,
                            data.firstName,
                            data.lastName
                        )
                    )
                if (data.dob.isNotEmpty()) {
                    val dob = data.dob.split("-")
//                    binding.profileDOB.text =
//                        getString(
//                            R.string.dob_format,
//                            dob[2].toInt(),
//                            monthName[dob[1].toInt() - 1],
//                            dob[0].toInt()
//                        )
                }
                if (data.gender)
                    binding.male.isChecked = true
                else
                    binding.female.isChecked = true

                region = data.region_id

                if (regionsArray.contains(Region(data.region_id, "", ArrayList<Region>()))) {
                    val index =
                        regionsArray.indexOf(Region(data.region_id, "", ArrayList<Region>()))
                    (binding.activityTypeDropDownValue as? AutoCompleteTextView)?.setText(
                        regionsArray[index].name,
                        false
                    )
                }
            }

        }

    }

    override fun onRetry() {
        super.onRetry()
        viewModel.getProfileResponse()
    }

    override fun setUpObservers() {

        observeEvent(viewModel.profileResponse, ::handle)
        observeEvent(viewModel.updateResponse, ::handle)
        viewModel.regionsResponse.observe(viewLifecycleOwner)
        { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                    }

                    is Resource.Success -> {
                        regionsArray.addAll(resource.data as ArrayList<Region>)
                        if (region != -1)
                            if (regionsArray.contains(Region(region, "", ArrayList<Region>()))) {
                                val index =
                                    regionsArray.indexOf(Region(region, "", ArrayList<Region>()))
                                (binding.activityTypeDropDownValue as? AutoCompleteTextView)?.setText(
                                    regionsArray[index].name,
                                    false
                                )
                            }
                    }

                    is Resource.GenericError -> {
                        onGenericError(resource)
                    }

                    is Resource.Error -> {
                        onError(resource)
                    }
                }
            }
        }
    }


    override fun setUpUI() {
        _bottomSheetBinding = ChangeLanguageBottomsheetBinding.inflate(layoutInflater)
        bottomsheet = createBottomSheet(bottomSheetBinding.root)

        when (PrefManager.getLocale(requireContext())) {
            "ru" -> {
                Log.d("locale", "russian")
                binding.ru.isChecked = true
            }

            "uz" -> {
                Log.d("locale", "uzbek")
                binding.uz.isChecked = true
            }
        }

        uiMode = resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)

        binding.switch1.isChecked = uiMode == Configuration.UI_MODE_NIGHT_YES


        val arrayAdapter = TypedDropdownAdapter(
            requireContext(), R.layout.support_simple_spinner_dropdown_item,
            regionsArray
        )

        (binding.activityTypeDropDownValue as? AutoCompleteTextView)?.setAdapter(arrayAdapter)


        binding.activityTypeDropDownValue.setOnItemClickListener { adapterView, view, i, l ->
            arrayAdapter.getItem(i)?.let {
                region = it.id
            }
        }

    }

    override fun setUpOnClickListeners() {
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.switch1.setOnClickListener {
            PrefManager.saveTheme(requireContext(), binding.switch1.isChecked)
        }

        bottomSheetBinding.locale.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.ru -> {
                    changeLocale("ru")
                }

                R.id.uz -> {
                    changeLocale("uz")
                }
            }
        }

        binding.radioGroupLocale.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.ru -> {
                    changeLocale("ru")
                }

                R.id.uz -> {
                    changeLocale("uz")
                }
            }
        }

        binding.updateProfile.setOnClickListener {
            var firstName = ""
            var lastName = ""
            val fio = (binding.profileFullName.text).split(" ")
            firstName = fio[0]
            Log.d("fio", "$fio")
            if (fio.size > 1) {
                lastName = fio[1]
            }

            var inn = binding.profileINN.text.trim().toString()
            val storeName = binding.profileStoreName.text.toString()

            var year = ""
            var month = 0
            var day = ""

//            val birthday = (binding.profileDOB.text).split(" ")
//
//
//            if (birthday.size > 2) {
//                year = birthday[2]
//                val monthName = resources.getStringArray(R.array.month)
//                month = monthName.indexOf(birthday[1]) + 1
//                day = birthday[0]
//            }


            when {
                firstName.isBlank() -> {
                    binding.profileFullName.error = getString(R.string.error_name)
                }

                lastName.isBlank() -> {
                    binding.profileFullName.error = getString(R.string.error_last_name)
                }

                storeName.isBlank() -> {
                    binding.profileStoreName.error = "Укажите название магазина"
                }

                inn.length != 9 -> {
                    binding.profileINN.error = "Укажите ИНН"
                }

                region == -1 -> {
                    binding.changeRegion.error = getString(R.string.warning_region)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.warning_region),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {
                    viewModel.getUpdateProfileResponse(
                        firstName,
                        lastName,
                        inn = inn.toInt(),
                        name = storeName,
                        region
                    )
                }

                //                    year.isBlank()->{
//                        binding.profileDOB.error = getString(R.string.error_year)
//                    }
//                    month==0->{
//                        binding.profileDOB.error = getString(R.string.error_month)}
//                    day.isBlank()-> {
//
//                        binding.profileDOB.error = getString(R.string.error_day)
//                    }
            }
        }




        binding.changeLanguage.setOnClickListener {
            //  bottomsheet.show()
        }

//        binding.profileDOB.setOnClickListener {
//            val dpd = DatePickerDialog(
//                requireContext(), R.style.datePicker,
//                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
//
//                    val monthName = resources.getStringArray(R.array.month)
//                    // Display Selected date in textbox
//                    binding.profileDOB.text =
//                        getString(R.string.dob_format, dayOfMonth, monthName[monthOfYear], year)
//                }, year, month, day
//            ).show()
//        }

    }

    private fun changeLocale(locale: String) {
        val oldLocale = PrefManager.getLocale(requireContext())
        if (oldLocale != locale) {
            PrefManager.saveLocale(requireContext(), locale.toLowerCase(Locale.ROOT))
            ActivityCompat.recreate(requireActivity())
        }
    }
}