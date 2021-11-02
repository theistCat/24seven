package uz.usoft.a24seven.network.models

import androidx.room.PrimaryKey

data class CartResponse(
    val total:Int,
    val delivery_price:Int,
    val products:List<CartItemImpl>
)

data class CartItemImpl(
    val id: Int=-1,
    val count:Int,
    val product:Product
)