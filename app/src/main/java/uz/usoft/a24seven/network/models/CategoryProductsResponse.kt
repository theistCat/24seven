package uz.usoft.a24seven.network.models

data class CategoryProductsResponse(
    val pagination: CategoryPagination,
    val items: List<Product>
)

data class CategoryPagination(
    val current: Int? = 0,
    val previous: Int? = 0,
    val next: Int? = 0,
    val total: Int? = 0,
    val perPage: Int? = 0
)


