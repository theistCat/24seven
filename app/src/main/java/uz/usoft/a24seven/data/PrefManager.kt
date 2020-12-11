package uz.usoft.kidya.data

import android.content.Context
import android.content.SharedPreferences
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken

private const val TOKEN = "token"
private const val FIREBASE_TOKEN = "firebase_token"
private const val NAME = "name"
private const val EMAIL = "email"
private const val PHONE = "phone"
private const val TF_SEVEN = "24_seven"
private const val LOCALE_LANG = "localeLang"
private const val CARDS_LIST = "cardsList"
private const val CARD = "card"
private const val LOCATIONS_LIST = "locations_list"
private const val LOCATION = "loc_pin"
private const val CURRENT_ORDER_ID = "current_order_id"
private const val ORDER_STATUS = "order_status"
private const val ORDER_TITLE = "order_title"
private const val IS_LOGGED_IN = "is_logged_in"
private const val USER_ID=""

private fun getInstance(context: Context): SharedPreferences {
    return context.getSharedPreferences(TF_SEVEN, Context.MODE_PRIVATE)
}

fun Context.saveUserId(id: String) {
    getInstance(this).edit().putString(
        USER_ID, id
    ).apply()
}

fun Context.saveToken(token: String) {
    getInstance(this).edit().putString(
        TOKEN, token
    ).apply()
}

fun Context.getToken(): String {
    return getInstance(
        this
    ).getString(TOKEN, "") ?: ""
}

fun Context.getUserId(): String {
    return getInstance(
        this
    ).getString(USER_ID, "") ?: ""
}


fun Context.isUserLoggedIn(): Boolean {
    return getToken()
        .isNotEmpty()
}

fun Context.setLoggedOut() {
    getInstance(this).edit().putBoolean(IS_LOGGED_IN, false).apply()
}

fun Context.logout() {
    this.saveToken("")
    this.saveUserId("")
    this.setLoggedOut()
}

fun Context.isLoggedIn(): Boolean {
    return getInstance(this).getBoolean(IS_LOGGED_IN, false)
}

fun Context.loggedIn() {
    getInstance(this).edit().putBoolean(IS_LOGGED_IN, true).apply()
}

class PrefManager {
    companion object {


        private fun getInstance(context: Context): SharedPreferences {
            return context.getSharedPreferences(TF_SEVEN, Context.MODE_PRIVATE)
        }

        fun saveToken(context: Context, token: String) {
            getInstance(context).edit().putString(
                TOKEN, token
            ).apply()
        }

        fun saveUserId(context: Context, id: String) {
            getInstance(context).edit().putString(
                USER_ID, id
            ).apply()
        }

        fun getToken(context: Context): String {
            return getInstance(
                context
            ).getString(TOKEN, "")!!
        }

        fun removeToken(context: Context) {
            getInstance(context).edit().remove(
                TOKEN
            ).apply()
        }

        fun saveFirebaseToke(context: Context, firebase_token: String) {
            getInstance(context).edit().putString(
                FIREBASE_TOKEN, firebase_token
            ).apply()
        }

        fun getFirebaseToken(context: Context): String {
            return getInstance(
                context
            )
                .getString(FIREBASE_TOKEN, "")!!
        }

        fun saveName(context: Context, name: String) {
            getInstance(context).edit().putString(
                NAME, name
            ).apply()
        }

        fun getName(context: Context): String {
            return getInstance(
                context
            ).getString(NAME, "")!!
        }

        fun saveEmail(context: Context, email: String) {
            getInstance(context).edit().putString(
                EMAIL, email
            ).apply()
        }

        fun getEmail(context: Context): String {
            return getInstance(
                context
            ).getString(EMAIL, "")!!
        }

        fun savePhone(context: Context, phone: String) {
            getInstance(context).edit().putString(
                PHONE, phone
            ).apply()
        }

        fun getPhone(context: Context): String {
            return getInstance(
                context
            ).getString(PHONE, "")!!
        }

        fun getUserID(context: Context): String {
            return getInstance(
                context
            ).getString(USER_ID, "")!!
        }

        fun isUserLoggedIn(context: Context): Boolean {
            return getToken(context)
                .isNotEmpty()
        }

        fun saveLocale(context: Context, localeLang: String) {
            getInstance(context).edit().putString(LOCALE_LANG, localeLang).apply()
        }

        fun getLocale(context: Context): String {
            return getInstance(context).getString(LOCALE_LANG, "")!!
        }

//        fun saveLocations(context: Context, locations: ArrayList<Location>) {
//            val serializedList = Gson().toJson(locations)
//
//            getInstance(context).edit().putString(LOCATIONS_LIST, serializedList).apply()
//        }
//
//        fun getLocations(context: Context): ArrayList<Location>? {
//            val serializedList = getInstance(context).getString(LOCATIONS_LIST, null)
//            return if (serializedList != null) {
//                val locationsType = object : TypeToken<ArrayList<Location>>() {}.type
//                Gson().fromJson<ArrayList<Location>>(serializedList, locationsType)
//            } else {
//                null
//            }
//        }
//
//        fun saveLocation(context: Context, location: Location) {
//            val serializedLocation = Gson().toJson(location)
//
//            getInstance(context).edit().putString(LOCATION, serializedLocation).apply()
//        }
//
//        fun getLocation(context: Context): Location? {
//            val serializedLocation = getInstance(context).getString(LOCATION, null)
//            return if (serializedLocation != null) {
//                val locationType = object : TypeToken<Location>() {}.type
//                Gson().fromJson<Location>(serializedLocation, locationType)
//            } else {
//                null
//            }
//        }

        fun saveCurrentOrderId(context: Context, orderId: Int) {
            getInstance(context).edit().putInt(CURRENT_ORDER_ID, orderId).apply()
        }

        fun getCurrentOrderId(context: Context): Int {
            return getInstance(context).getInt(CURRENT_ORDER_ID, -1)
        }

        fun saveOrderStatus(context: Context, orderStatus: Boolean) {
            getInstance(context).edit().putBoolean(ORDER_STATUS, orderStatus).apply()
        }

        fun isOrderActive(context: Context): Boolean {
            return getInstance(context).getBoolean(ORDER_STATUS, false)
        }


        fun saveOrderTitle(context: Context, orderTitle: String) {
            getInstance(context).edit().putString(ORDER_TITLE, orderTitle).apply()
        }

        fun getOrderTitle(context: Context): String {
            return getInstance(context).getString(ORDER_TITLE, "") ?: ""
        }


    }
}

