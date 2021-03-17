package uz.usoft.a24seven.network.models

import java.io.Serializable

class CategoriesResponse: ArrayList<CategoryObject>()

data class CategoryObject(
    val id: Int? = 0,
    val name: String? = "",
    val image:String?="",
    val products_count: Int? = 0,
    val parents: List<CategoryObject>
):Serializable

