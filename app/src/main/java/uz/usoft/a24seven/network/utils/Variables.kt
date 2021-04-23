package uz.usoft.a24seven.network.utils

import android.util.Log
import kotlin.properties.Delegates

object Variables {
    var isNetworkConnected: Boolean by Delegates.observable(false) { _, _, newValue ->
        Log.i("Network connectivity", "$newValue")
    }

    val sortBy:HashMap<Int,String> = hashMapOf(0 to "new", 1 to "popular", 2 to "cheap" ,3 to "expensive")
    val orderType:HashMap<Int,String> = hashMapOf(0 to "waiting", 1 to "active", 2 to "delivered")
    const val yandexKeySecond="814c74ce-2250-497f-9bee-c9b376d64431"
    const val yandexKey="1080018e-efa1-4eb6-821b-82f01120bf4c"
}