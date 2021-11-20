package uz.usoft.a24seven.network.utils

import com.google.gson.annotations.SerializedName

class RestError (
    val  statusCode: Int,
    val message:String,
    val error:String
)