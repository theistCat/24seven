package uz.usoft.a24seven.ui.profile

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.ChangeLanguageBottomsheetBinding
import uz.usoft.a24seven.databinding.FragmentProfileSettingsBinding
import uz.usoft.a24seven.utils.createBottomSheet
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.network.models.MockData
import uz.usoft.a24seven.network.models.ProfileResponse
import uz.usoft.a24seven.network.models.Region
import uz.usoft.a24seven.network.models.RegionResponse
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.observe
import uz.usoft.a24seven.utils.observeEvent
import java.util.*
import kotlin.collections.ArrayList


//Todo: disable update button if no update are made
class ProfileSettingsFragment : BaseFragment<FragmentProfileSettingsBinding>(FragmentProfileSettingsBinding::inflate) {

    private val viewModel:ProfileViewModel by viewModel()
    var uiMode :Int=0
    private val c: Calendar = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    var region:Int=1

    val regionsArray=ArrayList<Region>()

    private lateinit var bottomsheet : BottomSheetDialog
    private var _bottomSheetBinding : ChangeLanguageBottomsheetBinding?=null
    private val bottomSheetBinding get() = _bottomSheetBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getProfileResponse()
        viewModel.getRegions()
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)

        when(data){
        is ProfileResponse->{
            binding.profilePhone.setText(getString(R.string.phone_format, data.phone))

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
                binding.profileDOB.text =
                    getString(
                        R.string.dob_format,
                        dob[2].toInt(),
                        monthName[dob[1].toInt() - 1],
                        dob[0].toInt()
                    )
            }
            if (data.gender)
                binding.male.isChecked = true
            else
                binding.female.isChecked = true

            region=data.region_id

            if(regionsArray.contains(Region(data.region_id,"", ArrayList<Region>()))) {
                    val index=regionsArray.indexOf(Region(data.region_id,"", ArrayList<Region>()))
                (binding.activityTypeDropDownValue as? AutoCompleteTextView)?.setText(regionsArray[index].name, false)
            }
        }
            is List<*>->{
                regionsArray.addAll(data as ArrayList<Region>)
            }
        }

    }

    override fun onRetry() {
        super.onRetry()
        viewModel.getProfileResponse()
    }

    override fun setUpObservers() {

        observeEvent(viewModel.profileResponse,::handle)
        observeEvent(viewModel.updateResponse,::handle)
        viewModel.regionsResponse.observe(viewLifecycleOwner)
        {event->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
                        onSuccess(resource.data)
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
        _bottomSheetBinding=ChangeLanguageBottomsheetBinding.inflate(layoutInflater)
        bottomsheet=createBottomSheet(bottomSheetBinding.root)

        when(PrefManager.getLocale(requireContext())){
            "ru"->{
                Log.d("locale", "russian")
                binding.ru.isChecked=true
            }
            "uz"->{
                Log.d("locale", "uzbek")
                binding.uz.isChecked=true
            }
        }

        uiMode=resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)

        binding.switch1.isChecked= uiMode == Configuration.UI_MODE_NIGHT_YES


        val arrayAdapter = TypedDropdownAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item,
            regionsArray)

        (binding.activityTypeDropDownValue as? AutoCompleteTextView)?.setAdapter(arrayAdapter)


        binding.activityTypeDropDownValue.setOnItemClickListener { adapterView, view, i, l ->
                arrayAdapter.getItem(i)?.let {
                    region=it.id
                }
        }

    }

    override fun setUpOnClickListeners() {
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.switch1.setOnClickListener {
            PrefManager.saveTheme(requireContext(),binding.switch1.isChecked)
        }

        bottomSheetBinding.locale.setOnCheckedChangeListener { _, i ->
            when(i){
                R.id.ru->{changeLocale("ru")}
                R.id.uz->{changeLocale("uz")}
            }
        }

        binding.radioGroupLocale.setOnCheckedChangeListener { _, i ->
            when(i){
                R.id.ru->{changeLocale("ru")}
                R.id.uz->{changeLocale("uz")}
            }
        }

        binding.updateProfile.setOnClickListener {
            var firstName=""
            var lastName=""
            val fio=(binding.profileFullName.text).split(" ")
            firstName = fio[0]
            Log.d("fio","$fio")
            if(fio.size>1) {
                lastName = fio[1]
            }


            var year =""
            var month=0
            var day=""

            val birthday=(binding.profileDOB.text).split(" ")


            if(birthday.size>2) {
                year = birthday[2]
                val monthName = resources.getStringArray(R.array.month)
                month = monthName.indexOf(birthday[1]) + 1
                day = birthday[0]
            }


            when{
                firstName.isBlank()->{
                    binding.profileFullName.error = getString(R.string.error_name)}
                    lastName.isBlank()->{
                        binding.profileFullName.error = getString(R.string.error_last_name)
                    }
                    year.isBlank()->{
                        binding.profileDOB.error = getString(R.string.error_year)
                    }
                    month==0->{
                        binding.profileDOB.error = getString(R.string.error_month)}
                    day.isBlank()-> {

                        binding.profileDOB.error = getString(R.string.error_day)
                    }
            else->{






            val gender=binding.male.isChecked

            viewModel.getUpdateProfileResponse(
                firstName,
                lastName,
                "$year-${if(month<10) "0$month" else month}-${if(day.toInt()<10) "0$day" else day}",
                if (gender) 1 else 0,
                region
            )
            }
            }
        }




        binding.changeLanguage.setOnClickListener {
          //  bottomsheet.show()
        }

        binding.profileDOB.setOnClickListener {
            val dpd = DatePickerDialog(
                requireContext(), R.style.datePicker,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

                    val monthName = resources.getStringArray(R.array.month)
                    // Display Selected date in textbox
                    binding.profileDOB.text = getString(R.string.dob_format, dayOfMonth ,monthName[monthOfYear], year)
                }, year, month, day
            ).show()
        }

    }

    private fun changeLocale(locale:String){
        val oldLocale = PrefManager.getLocale(requireContext())
        if(oldLocale!=locale) {
            PrefManager.saveLocale(requireContext(), locale.toLowerCase(Locale.ROOT))
            ActivityCompat.recreate(requireActivity())
        }
    }
}