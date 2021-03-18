package uz.usoft.a24seven.network.models

data class HomeResponse(
    val compilations: List<Compilation>,
    val posts: List<Post>,
    val sliders: List<Any>
)