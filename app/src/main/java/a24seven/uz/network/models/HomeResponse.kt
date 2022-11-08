package a24seven.uz.network.models

data class HomeResponse(
    val compilations: List<Compilation>,
    val posts: List<Post>,
    val sliders: List<Banner>
)