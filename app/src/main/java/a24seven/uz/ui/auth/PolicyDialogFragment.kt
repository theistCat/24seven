package a24seven.uz.ui.auth

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import a24seven.uz.R
import a24seven.uz.databinding.FragmentPolicyDialogBinding

class PolicyDialogFragment : DialogFragment() {

    private var _binding:FragmentPolicyDialogBinding?=null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(),R.color.transparent)));
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentPolicyDialogBinding.inflate(layoutInflater,container,false)

        binding.close.setOnClickListener {
            this@PolicyDialogFragment.dismiss()
        }
        return binding.root
    }


}