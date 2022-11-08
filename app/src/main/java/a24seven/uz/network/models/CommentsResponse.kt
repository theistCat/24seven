package a24seven.uz.network.models

data class CommentsResponse(
    val items:List<Comment>,
    val pagination: Pagination
)