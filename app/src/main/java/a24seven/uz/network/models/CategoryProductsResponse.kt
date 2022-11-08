package a24seven.uz.network.models

data class CategoryProductsResponse(
    val pagination: Pagination,
    val items: List<Product>,
    val characteristics: List<Characteristics>?
)




