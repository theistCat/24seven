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
import uz.usoft.a24seven.utils.createBottomSheet
import java.util.*

class ProfileSettingsFragment : Fragment() {
    private val c: Calendar = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)
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
        return inflater.inflate(R.layout.fragment_profile_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomsheet=createBottomSheet(R.layout.changepasword_bottomsheet)
        changePassword.setOnClickListener {
            bottomsheet.show()
        }

        profileDOB.setOnClickListener {
            val dpd = DatePickerDialog(requireContext() ,R.style.datePicker,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                var monthName=""
                // Display Selected date in textbox
                when(monthOfYear)
                {
                    0->monthName="Января"
                    1->monthName="Февраля"
                    2->monthName="Марта"
                    3->monthName="Апреля"
                    4->monthName="Майа"
                    5->monthName="Июня"
                    6->monthName="Июля"
                    7->monthName="Августа"
                    8->monthName="Сентября"
                    9->monthName="Октября"
                    10->monthName="Ноября"
                    11->monthName="Декабря"
                }
                profileDOB.text = "$dayOfMonth $monthName $year"
            }, year, month, day).show()
        }
    }

}