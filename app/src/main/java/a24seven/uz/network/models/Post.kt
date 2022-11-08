package a24seven.uz.network.models

data class Post(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val views: Int,
    val language:String?,
    val content:String?,
    val url:String?
)