package uz.usoft.a24seven.network.models

data class Product(
    val category: Category,
    val comments_count: Int,
    val discount_percent: Int,
    val id: Int,
    val image: Image,
    val is_card: Boolean,
    val is_favorite: Boolean,
    val name: String,
    val price: Int,
    val price_discount: Int,
    val unit: Unit
)