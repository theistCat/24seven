package uz.usoft.a24seven.network.models

data class NewsResponse(
    val pagination: Pagination,
    val items:List<Post>
)