package a24seven.uz.network.utils

import org.json.JSONObject

data class ErrorResponse(
        var message: String = "Error",
        val result: Boolean = false,
        var statusCode: Int = 0,
        val code: Int = 0,
        @Transient
        var jsonResponse: JSONObject = JSONObject()
)