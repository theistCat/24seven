package a24seven.uz.network.utils

import com.squareup.moshi.Json

data class SuccessResponse(
        @Json(name = "status")
        var status: Boolean
)