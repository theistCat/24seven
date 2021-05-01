package uz.usoft.a24seven.ui.profile

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
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
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.ui.utils.BaseFragment
import uz.usoft.a24seven.utils.observeEvent
import java.util.*


//Todo: disable update button if no update are made
class ProfileSettingsFragment : BaseFragment<FragmentProfileSettingsBinding>(FragmentProfileSettingsBinding::inflate) {

    private val viewModel:ProfileViewModel by viewModel()
    var uiMode :Int=0
    private val c: Calendar = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    private lateinit var bottomsheet : BottomSheetDialog
    private var _bottomSheetBinding : ChangeLanguageBottomsheetBinding?=null
    private val bottomSheetBinding get() = _bottomSheetBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getProfileResponse()
    }

    override fun <T : Any> onSuccess(data: T) {
        super.onSuccess(data)

        data as ProfileResponse
        binding.profilePhone.setText(getString(R.string.phone_format, data.phone))

        val monthName = resources.getStringArray(R.array.month)

        if(data.first_name!=null&&data.last_name!=null)
            binding.profileFullName.setText(getString(R.string.full_name_format, data.firstName, data.lastName))
        if (data.dob.isNotEmpty()) {
            val dob= data.dob.split("-")
            binding.profileDOB.text =
                getString(R.string.dob_format, dob[2].toInt(), monthName[dob[1].toInt()-1], dob[0].toInt())
        }
        if(data.gender)
            binding.male.isChecked=true
        else
            binding.female.isChecked=true
    }

    override fun onRetry() {
        super.onRetry()
        viewModel.getProfileResponse()
    }

    override fun setUpObservers() {

        observeEvent(viewModel.profileResponse,::handle)
        observeEvent(viewModel.updateResponse,::handle)
    }



    override fun setUpUI() {
        _bottomSheetBinding=ChangeLanguageBottomsheetBinding.inflate(layoutInflater)
        bottomsheet=createBottomSheet(bottomSheetBinding.root)

        when(PrefManager.getLocale(requireContext())){
            "ru"->{
                Log.d("locale", "russian")
                bottomSheetBinding.ru.isChecked=true
            }
            "uz"->{
                Log.d("locale", "uzbek")
                bottomSheetBinding.uz.isChecked=true
            }
        }

        uiMode=resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)

        binding.switch1.isChecked= uiMode == Configuration.UI_MODE_NIGHT_YES

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
                    binding.profileFullName.error = "name"}
                    lastName.isBlank()->{
                        binding.profileFullName.error = "lastname"
                    }
                    year.isBlank()->{
                        binding.profileDOB.error = "year"
                    }
                    month==0->{
                        binding.profileDOB.error = "month"}
                    day.isBlank()-> {

                        binding.profileDOB.error = "day"
                    }
            else->{






            val gender=binding.male.isChecked

            viewModel.getUpdateProfileResponse(
                firstName,
                lastName,
                "$year-${if(month<10) "0$month" else month}-$day",
                if (gender) 1 else 0
            )
            }
            }
        }




        binding.changeLanguage.setOnClickListener {
            bottomsheet.show()
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