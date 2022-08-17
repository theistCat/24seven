package uz.usoft.a24seven.network.models

data class ProfileResponse(
    val id: Int,
    val avatar: String?,
    val phone: Long,
    val first_name: String?,
    val last_name: String?,
    val birth_day: String?,
    val gender: Boolean,
    val email: String?,
    val language: String?,
    val favorites_count: Int,
    val addresses_count: Int,
    val orders_count: Int,
    val region_id: Int,
    val name: String?,
    val inn: Int?
) {
    val lastName get() = last_name ?: ""
    val firstName get() = first_name ?: ""
    val dob get() = birth_day ?: ""
}