package uz.usoft.a24seven.network.utils

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor() :Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder();
        if(Variables.isNetworkConnected)
            return chain.proceed(builder.build())
        else
            throw NoConnectivityException()
    }
}