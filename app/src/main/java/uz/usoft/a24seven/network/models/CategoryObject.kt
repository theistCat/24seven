package uz.usoft.a24seven.network.models

data class CategoryObject(
    val id: Int,
    val name: String,
    val image: String?,
    val products_count: Int?,
    val parents: List<CategoryObject>
)