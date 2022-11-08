package a24seven.uz.ui.utils

import a24seven.uz.MainActivity
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.network.utils.Resource
import a24seven.uz.ui.loader.LoaderDialogFragment
import a24seven.uz.ui.noConnection.NoConnectionFragment
import a24seven.uz.utils.showSnackbar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


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

abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    private var noConnectionFragment: NoConnectionFragment? = null
    private var loadingDialog: LoaderDialogFragment? = null
    lateinit var mainActivity: MainActivity


    open fun getData() {}

    open fun setUpRecyclers() {}
    open fun setUpOnClickListeners() {}
    open fun setUpObservers() {}
    open fun setUpPagers() {}
    open fun setUpUI() {}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (loadingDialog == null) {
            loadingDialog = LoaderDialogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = requireActivity() as MainActivity
//        mainActivity.binding
        _binding = inflate.invoke(inflater, container, false)
        init()
        return binding.root
    }


    fun setTitle(title: String) {
        mainActivity.setTitle(title)
    }


    fun init() {
        setUpUI()
        setUpRecyclers()
        setUpOnClickListeners()
        setUpPagers()
        setUpObservers()
    }


    //TODO: Move this to main activity
    fun showLoadingDialog() {
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

    fun showNoConnectionDialog(retryCall: () -> Unit) {
        val fm = requireActivity().supportFragmentManager
        if (noConnectionFragment == null) {
            noConnectionFragment = NoConnectionFragment(retryCall)
        }
        if (!noConnectionFragment!!.isAdded) {
            noConnectionFragment!!.show(fm, "NoConnection")
            noConnectionFragment!!.isCancelable = false
        }
    }

    fun hideNoConnectionDialog() {
        noConnectionFragment?.dismiss()
    }


    open fun <T : Any> onSuccess(data: T) {
        hideNoConnectionDialog()
        hideLoadingDialog()
    }

    open fun onLoading() {

        hideNoConnectionDialog()
        showLoadingDialog()
    }

    open fun onRetry() {

        hideNoConnectionDialog()
    }

    open fun onGenericError(resource: Resource.GenericError) {
        hideLoadingDialog()
        if (resource.errorResponse.jsonResponse.has("error"))
            showSnackbar(resource.errorResponse.jsonResponse.getString("error"))
        else showSnackbar(resource.errorResponse.message)
    }

    open fun onError(resource: Resource.Error) {
        hideLoadingDialog()
        if (resource.exception is NoConnectivityException) {
            showNoConnectionDialog(this::onRetry)
        } else resource.exception.message?.let { it1 -> showSnackbar(it1) }
    }

    //TODO: make onSuccess reusable
    fun <T : Any> handle(event: Event<Resource<T>>) {
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
        _binding = null
    }

}