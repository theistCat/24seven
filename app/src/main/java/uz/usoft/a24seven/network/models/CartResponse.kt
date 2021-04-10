package uz.usoft.a24seven.network.models

data class CartResponse(
    val total:Int,
    val products:List<Product>
)