package uz.usoft.a24seven.ui.profile

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import kotlinx.android.synthetic.main.fragment_profile_settings.*
import uz.usoft.a24seven.R
import uz.usoft.a24seven.databinding.FragmentProfileBinding
import uz.usoft.a24seven.databinding.FragmentProfileSettingsBinding
import uz.usoft.a24seven.utils.createBottomSheet
import java.util.*

class ProfileSettingsFragment : Fragment() {
    private val c: Calendar = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
    private var _binding: FragmentProfileSettingsBinding? = null
    private val binding get() = _binding!!
    private val bottomsheet = createBottomSheet(R.layout.changepasword_bottomsheet)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)
        setUpOnClickListener()
        return binding.root
    }

    private fun setUpOnClickListener() {
        binding.changePassword.setOnClickListener {
            bottomsheet.show()
        }
        binding.profileDOB.setOnClickListener {
            val dpd = DatePickerDialog(
                requireContext(), R.style.datePicker,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    var monthName = ""
                    // Display Selected date in textbox
                    when (monthOfYear) {
                        0 -> monthName = "Января"
                        1 -> monthName = "Февраля"
                        2 -> monthName = "Марта"
                        3 -> monthName = "Апреля"
                        4 -> monthName = "Майа"
                        5 -> monthName = "Июня"
                        6 -> monthName = "Июля"
                        7 -> monthName = "Августа"
                        8 -> monthName = "Сентября"
                        9 -> monthName = "Октября"
                        10 -> monthName = "Ноября"
                        11 -> monthName = "Декабря"
                    }
                    binding.profileDOB.text = "$dayOfMonth $monthName $year"
                }, year, month, day
            ).show()
        }
    }
}