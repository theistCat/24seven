package uz.usoft.a24seven.network.models

import java.io.Serializable

data class Image(
    val id: Int,
    val path: String,
    val path_thumb: String
):Serializable