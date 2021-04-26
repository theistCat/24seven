package uz.usoft.a24seven.network.models

data class CategoryProductsResponse(
    val pagination: Pagination,
    val items: List<Product>,
    val characteristics: List<Characteristics>
)




