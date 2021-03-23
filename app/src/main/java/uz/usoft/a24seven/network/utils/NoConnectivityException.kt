package uz.usoft.a24seven.network.utils

import android.content.Context
import uz.usoft.a24seven.MainApplication
import uz.usoft.a24seven.R
import java.io.IOException

class NoConnectivityException() : IOException(){
    override val message: String?
    get() = MainApplication.mContext.getString(R.string.no_connectivity_exception)
}