package a24seven.uz.network.models

data class NewsResponse(
    val pagination: Pagination,
    val items:List<Post>
)