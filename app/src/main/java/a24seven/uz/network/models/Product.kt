package a24seven.uz.network.models

import java.io.Serializable

data class Product(
    val id: Int,
    val name: String,
    val image: Image?,
    val price: Int,
    val price_discount: Int?,
    val description: String?,
    val unit: Unit,
    var is_favorite: Boolean,
    val category: Category?,
    val characteristics: List<Characteristics>?,
    val is_card: Boolean,
    var is_cart: Boolean,
    var cart_count: Int,
    val comments: List<Any>?,
    val comments_count: Int,
    val discount_percent: Int,
    val images: List<Image>?,
    val products_related: List<Product>?,
    var product_count:Int
):Serializable
{
    @Transient
    var count:Int=0
}
