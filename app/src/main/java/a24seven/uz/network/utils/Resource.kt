package a24seven.uz.network.utils

import okhttp3.ResponseBody
import java.io.IOException
import java.lang.Exception

sealed class Resource<out T : Any> {
    object Loading : Resource<Nothing>()
    data class Success<out T : Any>(val data: T) : Resource<T>()
    data class Error(val exception: Exception) : Resource<Nothing>()
    data class GenericError(val errorResponse: ErrorResponse) : Resource<Nothing>()
}


fun ResponseBody?.toException() = IOException(this?.string())