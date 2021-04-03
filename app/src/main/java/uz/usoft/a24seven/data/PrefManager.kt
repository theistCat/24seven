package uz.usoft.a24seven.data

import android.content.Context
import android.content.SharedPreferences


class PrefManager {
    companion object {
        private const val TF_SEVEN = "tf_seven"
        private const val TOKEN = ""
        private const val FIREBASE_TOKEN = "firebase_token"
        private const val LOCALE_LANG = "localeLang"
        private const val PAYMENT_METHOD="PayMe"

        private fun getInstance(context: Context): SharedPreferences {
            return context.getSharedPreferences(TF_SEVEN, Context.MODE_PRIVATE)
        }

        fun saveToken(context: Context, token: String) {
            getInstance(context).edit().putString(
                TOKEN, token
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

        fun savePaymentMethod(context: Context, paymentMethod: String) {
            getInstance(context).edit().putString(
                PAYMENT_METHOD, paymentMethod
            ).apply()
        }

        fun getPaymentMethod(context: Context): String {
            return getInstance(
                context
            ).getString(PAYMENT_METHOD, "PayMe")!!
        }


        fun saveLocale(context: Context, localeLang: String) {
            getInstance(context).edit().putString(LOCALE_LANG, localeLang).apply()
        }

        fun getLocale(context: Context): String {
            return getInstance(context).getString(LOCALE_LANG, "ru")!!
        }

        fun isLoggedIn(context: Context):Boolean{
             return getToken(context).isNotBlank()
        }

        fun logout(context: Context){
            getInstance(context).edit().putString(
                TOKEN, ""
            ).apply()
        }

    }
}

