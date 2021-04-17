package uz.usoft.a24seven.network.utils

import android.util.Log
import kotlin.properties.Delegates

object Variables {
    var isNetworkConnected: Boolean by Delegates.observable(false) { _, _, newValue ->
        Log.i("Network connectivity", "$newValue")
    }

    val sortBy:HashMap<Int,String> = hashMapOf(0 to "new", 1 to "popular", 2 to "cheap" ,3 to "expensive")
    val orderType:HashMap<Int,String> = hashMapOf(0 to "waiting", 1 to "active", 2 to "delivered")

}