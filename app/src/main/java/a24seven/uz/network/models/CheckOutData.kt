package a24seven.uz.network.models

import java.io.Serializable

data class CheckOutData(
    val productList:HashMap<String,Int>,
    val order_price:Int,
    val total:Int,
    val delivery_fee:Int=0,
):Serializable