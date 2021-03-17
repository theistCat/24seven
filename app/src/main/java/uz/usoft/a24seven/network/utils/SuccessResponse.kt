package uz.usoft.a24seven.network.utils

import com.squareup.moshi.Json

data class SuccessResponse(
        @Json(name = "status")
        var status: Boolean
)