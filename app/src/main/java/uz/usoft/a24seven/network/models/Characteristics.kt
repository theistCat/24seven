package uz.usoft.a24seven.network.models

import java.io.Serializable


class Characteristics (
    val id: Int,
    val name: String,
    val value:String?,
    val type: String?,
    val attributes: List<String>?
):Serializable
