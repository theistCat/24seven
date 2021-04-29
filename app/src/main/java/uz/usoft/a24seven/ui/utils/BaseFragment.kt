package uz.usoft.a24seven.ui.utils

import android.icu.text.CaseMap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.network.utils.ErrorResponse
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.ui.loader.LoaderDialogFragment
import uz.usoft.a24seven.ui.noConnection.NoConnectionFragment
import uz.usoft.a24seven.utils.showSnackbar


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T
//
//abstract class BaseFragment<VB: ViewBinding>(
//    private val inflate: Inflate<VB>
//) : Fragment() {
//
//    private var _binding: VB? = null
//    val binding get() = _binding!!
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        _binding = inflate.invoke(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }

abstract class BaseFragment<VB: ViewBinding> (private val inflate: Inflate<VB> ): Fragment()  {

    private var _binding: VB? = null
    val binding get() = _binding!!

    var noConnectionFragment: NoConnectionFragment?=null
    var loadingDialog: LoaderDialogFragment?=null
    lateinit var  mainActivity:MainActivity


    open fun getData() {}

    open fun setUpRecyclers(){}
    open fun setUpOnClickListeners(){}
    open fun setUpObservers(){}
    open fun setUpPagers(){}
    open fun setUpUI(){}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (loadingDialog == null) {
            loadingDialog = LoaderDialogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mainActivity=requireActivity() as MainActivity
//        mainActivity.binding
        _binding = inflate.invoke(inflater, container, false)
        init()
        return binding.root
    }



    fun setTitle(title: String){
        mainActivity.setTitle(title)
    }



    fun init()
    {
        setUpUI()
        setUpRecyclers()
        setUpOnClickListeners()
        setUpPagers()
        setUpObservers()
    }


    //TODO: Move this to main activity
    fun showLoadingDialog(){
//        val fm = childFragmentManager
//        if(!(loadingDialog!!.isAdded)) {
//            if (fm.findFragmentByTag("Loading") == null) {
//                loadingDialog!!.show(fm, "Loading")
//                loadingDialog!!.isCancelable = false
//            }
//        }
        (requireActivity() as MainActivity).showLoadingDialog()
    }
    fun hideLoadingDialog() {
//        val loaderDialogFragment=childFragmentManager.findFragmentByTag("Loading")
//        if(loaderDialogFragment!=null)
//            (loaderDialogFragment as LoaderDialogFragment).dismiss()
        (requireActivity() as MainActivity).hideLoadingDialog()
    }

    fun showNoConnectionDialog(retryCall:()->Unit){
        val fm = requireActivity().supportFragmentManager
        if (noConnectionFragment == null) {
                noConnectionFragment = NoConnectionFragment(retryCall)
        }
        if(!noConnectionFragment!!.isAdded) {
            noConnectionFragment!!.show(fm, "NoConnection")
            noConnectionFragment!!.isCancelable = false
        }
    }

    fun hideNoConnectionDialog() {
        noConnectionFragment?.dismiss()
    }



    open fun <T : Any>onSuccess(data: T){
        hideNoConnectionDialog()
        hideLoadingDialog()
    }

    open fun onLoading(){

        hideNoConnectionDialog()
        showLoadingDialog()
    }

    open fun onRetry(){

        hideNoConnectionDialog()
    }

    open fun onGenericError(resource: Resource.GenericError){
        hideLoadingDialog()
        showSnackbar(resource.errorResponse.jsonResponse.getString("error"))
    }

    open fun onError(resource: Resource.Error){
        hideLoadingDialog()
        if (resource.exception is NoConnectivityException) {
            showNoConnectionDialog(this::onRetry)
        } else resource.exception.message?.let { it1 -> showSnackbar(it1) }
    }

    //TODO: make onSuccess reusable
    fun <T : Any>handle(event: Event<Resource<T>>){
        event.getContentIfNotHandled()?.let { resource ->
            when (resource) {
                is Resource.Loading -> {
                    onLoading()
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

    override fun onResume() {
        super.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}