package uz.usoft.a24seven.network.models

data class Product(
    val id: Int,
    val name: String,
    val image: Image?,
    val price: Int,
    val price_discount: Int?,
    val unit: Unit,
    var is_favorite: Boolean,
    val category: Category,
    val characteristics: List<Any>?,
    val is_card: Boolean,
    val comments: List<Any>?,
    val comments_count: Int,
    val discount_percent: Int,
    val images: List<Image>?,
    val products_related: List<Product>?
)
