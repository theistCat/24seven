package uz.usoft.a24seven.network.models

data class Compilation(
    val id: Int,
    val products: List<Product>,
    val title: String
)