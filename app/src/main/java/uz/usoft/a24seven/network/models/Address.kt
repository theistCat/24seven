package uz.usoft.a24seven.network.models

data class Address(
    val id: Int,
    val name:String?,
    val city:String?,
    val region:String?,
    val address:String?,
    val location: LocPoint?,
    val city_id: Int?,
    val region_id: Int?,
)