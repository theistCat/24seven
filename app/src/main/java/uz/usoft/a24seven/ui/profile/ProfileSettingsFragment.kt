package uz.usoft.a24seven.ui.profile

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.ChangeLanguageBottomsheetBinding
import uz.usoft.a24seven.databinding.FragmentProfileSettingsBinding
import uz.usoft.a24seven.utils.createBottomSheet
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.ui.utils.BaseFragment
import java.util.*
import kotlin.coroutines.Continuation

class ProfileSettingsFragment : BaseFragment<FragmentProfileSettingsBinding>(FragmentProfileSettingsBinding::inflate) {
    private val c: Calendar = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    private lateinit var bottomsheet : BottomSheetDialog
    private var _bottomSheetBinding : ChangeLanguageBottomsheetBinding?=null
    private val bottomSheetBinding get() = _bottomSheetBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
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

        val uiMode=resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)

        binding.switch1.isChecked= uiMode == Configuration.UI_MODE_NIGHT_YES

    }

    override fun setUpOnClickListeners() {
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        bottomSheetBinding.locale.setOnCheckedChangeListener { _, i ->
            when(i){
                R.id.ru->{changeLocale("ru")}
                R.id.uz->{changeLocale("uz")}
            }
        }




        binding.changeLanguage.setOnClickListener {
            bottomsheet.show()
        }
        binding.profileDOB.setOnClickListener {
            val dpd = DatePickerDialog(
                requireContext(), R.style.datePicker,
                DatePickerDialog.OnDateSetListener { _s, year, monthOfYear, dayOfMonth ->

                    val monthName = resources.getStringArray(R.array.month)
                    // Display Selected date in textbox
                    binding.profileDOB.text = "$dayOfMonth ${monthName[monthOfYear]} $year"
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