package a24seven.uz.network.utils

import android.util.Log
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.Converter
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException

suspend fun <T : Any> safeApiCall(
    apiCall: suspend () -> Response<T>
): Resource<T> {
    try {
        val response = apiCall()
        return if (response.isSuccessful) {
            if(response.body()!=null) Resource.Success<T>(response.body() as T) else Resource.Success<T>(
                "" as T
            )
        } else {
            if (response.errorBody() != null) {
                val retrofit = Retrofit.Builder().baseUrl("http://api.24seven.uz/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val converter: Converter<ResponseBody, RestError> = retrofit.responseBodyConverter(
                    RestError::class.java,
                    arrayOfNulls<Annotation>(0)
                )
                val _object: RestError? = converter.convert(response.errorBody());

                Log.d("ERRORTAG", _object?.message.toString())

                val errorResponse = ErrorResponse(message = _object?.message ?: "")

                errorResponse.statusCode = response.code()
                if (errorResponse.jsonResponse.toString().contains("message")) {
                    if (errorResponse.jsonResponse.has("error")) {
                        errorResponse.message =
                            errorResponse.jsonResponse.getJSONObject("error").getString(
                                "message"
                            )
                    }
                }

                Resource.GenericError(errorResponse)
            } else
                Resource.GenericError(ErrorResponse("Unknown error"))

//            if (response.errorBody() != null) {
//                val errorResponse = ErrorResponse(jsonResponse = JSONObject(response.errorBody()!!.toString()))
//                errorResponse.statusCode = response.code()
//                if (errorResponse.jsonResponse.toString().contains("message")) {
//                    if (errorResponse.jsonResponse.has("error")) {
//                        errorResponse.message = errorResponse.jsonResponse.getJSONObject("error").getString("message")
//                    }
//                }
//                Resource.GenericError(errorResponse)
//            } else
//                Resource.GenericError(ErrorResponse("Unknown error"))
        }
    } catch (throwable: Throwable) {
        Log.d("ErrorTag", throwable.message.toString())
        return when (throwable) {
            is ConnectException,
            is NoConnectivityException -> {
                Resource.Error(NoConnectivityException())
            }
            is HttpException -> {
                val errorResponse: ErrorResponse = throwable.response()?.body() as ErrorResponse
                Resource.GenericError(errorResponse)
            }
            is IOException -> {
                Resource.Error(Exception("IOException: " + throwable.message))
            }
            else -> {
                Resource.Error(Exception(throwable.message))
            }
        }
    }
}