package uz.usoft.a24seven.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.usoft.a24seven.network.models.CategoriesResponse
import uz.usoft.a24seven.network.models.CategoryObject
import uz.usoft.a24seven.network.models.CategoryProductsResponse

interface SevenApi {
    @GET("/")
    suspend fun getHome(): Response<Any>

    @GET("/categories")
    suspend fun getCategories(): Response<CategoriesResponse>

    @GET("/categories/{category_id}")
    suspend fun getCategoryProducts(
        @Path("category_id") category_id: Int,
        @Query("page") page: Int,
        @Query("orderBy") orderBy: String
    ): CategoryProductsResponse


}
