package uz.usoft.a24seven.network.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.ui.noConnection.NoConnectionFragment

abstract class BaseFragment : Fragment() , NoConnectionDialogListener{

    var noConnectionFragment: NoConnectionFragment?=null
    lateinit var  mainActivity:MainActivity

    abstract fun setUpRecyclers()
    abstract fun setUpOnClickListeners()
    abstract fun setUpObservers()
    abstract fun setUpPagers()
    abstract fun setUpData()

    fun superOnCreateView(
        binding: ViewBinding
    ): View? {
        mainActivity=requireActivity() as MainActivity
        init()
        return binding.root
    }

    fun init()
    {
        setUpRecyclers()
        setUpOnClickListeners()
        setUpPagers()
        setUpObservers()
        setUpData()
    }

    fun showNoConnectionDialog(){
            if (noConnectionFragment == null) {
                noConnectionFragment = NoConnectionFragment(this)
                val fm = requireActivity().supportFragmentManager
                noConnectionFragment!!.show(fm, "NoConnection")
                noConnectionFragment!!.isCancelable = false
            }
    }

    fun hideNoConnectionDialog() {
        noConnectionFragment?.dismiss()
    }
}