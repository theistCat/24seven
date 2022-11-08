package a24seven.uz.ui.noConnection

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import a24seven.uz.MainActivity
import a24seven.uz.R
import a24seven.uz.databinding.FragmentNoConnectionBinding

class NoConnectionFragment(val retryCall:()->Unit) : DialogFragment() {
    private var _binding: FragmentNoConnectionBinding? = null
    private val binding get() = _binding!!

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
        _binding= FragmentNoConnectionBinding.inflate(layoutInflater,container,false)
        val mainActivity=requireActivity() as MainActivity
        mainActivity.hideBottomNavigation()
        mainActivity.hideToolbar()

        setUpClickListeners()

        return binding.root
    }

    private fun setUpClickListeners() {
        binding.tryAgain.setOnClickListener {
            retryCall.invoke()
        }
    }

    companion object {
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(),R.color.background)));
    }
}