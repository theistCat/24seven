package uz.usoft.a24seven.network.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONException
import uz.usoft.a24seven.MainActivity
import uz.usoft.a24seven.data.PrefManager
import uz.usoft.a24seven.ui.auth.AuthActivity

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