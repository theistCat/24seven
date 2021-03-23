package uz.usoft.a24seven.network.models

data class CommentsResponse(
    val items:List<Comment>,
    val pagination: Pagination
)