package a24seven.uz.network.models

import java.io.Serializable


class Characteristics (
    val id: Int,
    val name: String,
    val value:String?,
    val type: String?,
    val attributes: List<String>?
):Serializable
