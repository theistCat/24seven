package uz.usoft.a24seven.network.models

import kotlin.reflect.jvm.internal.impl.name.ClassId

data class CategoryProductsResponse(
    val pagination: CategoryPagination,
    val items: List<CategoryProductsItems>
)

data class CategoryPagination(
    val current: Int? = 0,
    val previous: Int? = 0,
    val next: Int? = 0,
    val total: Int? = 0,
    val perPage: Int? = 0
)

data class CategoryProductsItems(
    val id: Int? = 0,
    val name: String? = "",
    val image: Images,
    val price: Int? = 0,
    val price_discount: Int? = 0,
    val unit: Unit,
    val is_favorite: Boolean,
    val is_card: Boolean,
    val discount_percent: Int? = 0,
    val comments_count: Int? = 0,
    val category: Category
)

data class Images(
    val id: Int? = 0,
    val path: String? = "",
    val path_thumb: String? = ""
)

data class Unit(
    val id: Int? = 0,
    val name: String? = "",
    val count: Int? = 0
)

data class Category(
    val id: Int? = 0,
    val name: String? = ""
)
