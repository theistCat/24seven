package uz.usoft.a24seven.network.utils

import android.util.Log
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import uz.usoft.a24seven.MainApplication
import java.net.ConnectException

suspend fun <T : Any> safeApiCall(
    apiCall: suspend () -> Response<T>
): Resource<T> {
    try {
        val response = apiCall()
        if (response.isSuccessful && response.body() != null) {
//            var json = Gson()
//            if (response.body() != null) {
//                var customObject = response.body() as T
//                if (customObject != null) {
//                    val castedObject = json.toJson(customObject)
//                    val jsonMoshi = Moshi.Builder().build();
//                    val moshiConvert = jsonMoshi.adapter(BusinessInnResponse::class.java)
//
//                    Log.d("ErrorTag:", "Moshi: ${moshiConvert.toJson(response.body() as BusinessInnResponse)}")
//                    Log.d("ErrorTag:", "CastedObject: $castedObject")
//                } else
//                    Log.d("ErrorTag:", "Null")
//            } else
//                Log.d("ErrorTag:", "If responsbody null raw: " + response.raw().toString())
            return Resource.Success<T>(response.body() as T)
        } else {
            return if (response.errorBody() != null) {
                val errorResponse = ErrorResponse(jsonResponse = JSONObject(response.errorBody()!!.toString()))
                errorResponse.statusCode = response.code()
                if (errorResponse.jsonResponse.toString().contains("message")) {
                    if (errorResponse.jsonResponse.has("error")) {
                        errorResponse.message = errorResponse.jsonResponse.getJSONObject("error").getString("message")
                    }
                }
                Resource.GenericError(errorResponse)
            } else
                Resource.GenericError(ErrorResponse("Unknown error"))
        }
    } catch (throwable: Throwable) {
        Log.d("ErrorTag", throwable.message.toString())
        when (throwable) {
            is ConnectException,
            is NoConnectivityException -> {
                return Resource.Error(NoConnectivityException())
            }
            is HttpException -> {
                val errorResponse: ErrorResponse = throwable.response()?.body() as ErrorResponse
                return Resource.GenericError(errorResponse)
            }
            is IOException -> {
                return Resource.Error(Exception("IOException: " + throwable.message))
            }
            else -> {
                return Resource.Error(Exception(throwable.message))
            }
        }
    }
}