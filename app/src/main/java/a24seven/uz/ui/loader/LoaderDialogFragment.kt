package a24seven.uz.ui.loader

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import a24seven.uz.R
import a24seven.uz.databinding.FragmentLoaderDialogBinding

class LoaderDialogFragment : DialogFragment() {

    private var _binding:FragmentLoaderDialogBinding?=null
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
        _binding= FragmentLoaderDialogBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoaderDialogFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(),R.color.background_transparent)))

        val loadingAnimation=AnimationUtils.loadAnimation(requireContext(),R.anim.loading)
        binding.imageView3.startAnimation(loadingAnimation)
    }
}