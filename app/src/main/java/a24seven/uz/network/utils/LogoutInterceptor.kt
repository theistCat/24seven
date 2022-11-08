package a24seven.uz.network.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONException
import a24seven.uz.MainActivity
import a24seven.uz.data.PrefManager
import a24seven.uz.ui.auth.AuthActivity

class LogoutInterceptor(val context: Context) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val response: Response = chain.proceed(chain.request())

        val globJson = response.body!!.string()
        try {
            if (response.code
                == 401
            ) {
                PrefManager.logout(context)
                Log.d("Result","caught")
                MainActivity.openAuthActivityCustom.launch(Intent(context, AuthActivity::class.java)
            )

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }


        // Re-create the response before returning it because body can be read only once
        return response.newBuilder()
            .body( globJson.toResponseBody(response.body!!.contentType())).build()
    }

}