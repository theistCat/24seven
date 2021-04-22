package uz.usoft.a24seven.network.models

data class CartResponse(
    val total:Int,
    val delivery_price:Int,
    val products:List<Product>
)