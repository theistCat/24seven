package a24seven.uz.network.utils

import android.util.Log
import kotlin.properties.Delegates

object Variables {
    var isNetworkConnected: Boolean by Delegates.observable(false) { _, _, newValue ->
        Log.i("Network connectivity", "$newValue")
    }

    val sortBy: HashMap<String, String> =
        hashMapOf("sort[id]" to "desc", "sort[views]" to "desc", "sort[price]" to "asc", "sort[price]" to "desc")
    val orderType: HashMap<Int, String> =
        hashMapOf(0 to "waiting", 1 to "active", 2 to "delivered", 3 to "cancelled", 4 to "closed")
    val characteristicType: HashMap<Int, String> =
        hashMapOf(0 to "text", 1 to "number", 2 to "checkbox", 3 to "select")

    const val yandexKeySecond = "814c74ce-2250-497f-9bee-c9b376d64431"
    const val yandexKey = "1080018e-efa1-4eb6-821b-82f01120bf4c"

    const val fromAddAddress = "addAddress"
    const val fromEditAddress = "editAddress"
    const val fromCheckout = "checkout"

    var updateNeeded: Boolean by Delegates.observable(false) { _, _, newValue ->
        Log.i("Update request", "$newValue")
    }
}