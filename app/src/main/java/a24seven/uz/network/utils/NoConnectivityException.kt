package a24seven.uz.network.utils

import a24seven.uz.MainApplication
import a24seven.uz.R
import java.io.IOException

class NoConnectivityException() : IOException(){
    override val message: String?
    get() = MainApplication.mContext.getString(R.string.no_connectivity_exception)
}