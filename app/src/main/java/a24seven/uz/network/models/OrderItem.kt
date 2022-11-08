package a24seven.uz.network.models

data class OrderItem (
    val id:Int,
    val name:String,
    val image: Image,
    val price: String,
    val count:Int,
    val unit: Unit,
)