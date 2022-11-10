package a24seven.uz.network

import a24seven.uz.network.models.Address
import a24seven.uz.network.models.AddressResponse
import a24seven.uz.network.models.CartResponse
import a24seven.uz.network.models.CategoryObject
import a24seven.uz.network.models.CategoryProductsResponse
import a24seven.uz.network.models.CoinProduct
import a24seven.uz.network.models.Comment
import a24seven.uz.network.models.CommentsResponse
import a24seven.uz.network.models.HomeResponse
import a24seven.uz.network.models.NewsResponse
import a24seven.uz.network.models.Order
import a24seven.uz.network.models.OrderCoinProduct
import a24seven.uz.network.models.OrderResponse
import a24seven.uz.network.models.Post
import a24seven.uz.network.models.Product
import a24seven.uz.network.models.ProfileResponse
import a24seven.uz.network.models.Region
import a24seven.uz.network.models.VerifyResponse
import retrofit2.Response
import retrofit2.http.*
import a24seven.uz.network.models.*

interface SevenApi {
    @GET("/")
    suspend fun getHome(): Response<HomeResponse>

    //region Auth
    @FormUrlEncoded
    @POST("/oauth/verify")
    suspend fun verifyCode(
        @Field("phone") phone: String,
        @Field("verify_code") code: String
    ): Response<VerifyResponse>


    @FormUrlEncoded
    @POST("/oauth/login")
    suspend fun authFirstStep(
        @Field("phone") phone: String
    ): Response<Any>

    @POST("/oauth/logout")
    suspend fun logout(): Response<Void>
    //endregion

    //region Categories
    @GET("/categories")
    suspend fun getCategories(): Response<List<CategoryObject>>

    @GET("/categories/{category_id}")
    suspend fun getCategoryProducts(
        @Path("category_id") category_id: Int,
        @Query("page") page: Int,
        @QueryMap queryMap: Map<String, String>,
    ): CategoryProductsResponse

    //{{app}}/categories/10/filter?page=1&orderBy=new

    @FormUrlEncoded
    @POST("/categories/{category_id}/filter")
    suspend fun getFilteredCategoryProducts(
        @Path("category_id") category_id: Int,
        @Query("page") page: Int,
        @QueryMap queryMap: Map<String, String>,
        @FieldMap products: HashMap<String, String>,
    ): CategoryProductsResponse
    //endregion

    //region Products
    @GET("/product/{product_id}")
    suspend fun getProduct(
        @Path("product_id") product_id: Int,
    ): Response<Product>

    @GET("/coin-product/")
    suspend fun getCoinProducts(): Response<List<CoinProduct>>


    @FormUrlEncoded
    @POST("/coin-product/order")
    suspend fun orderCoins(
        @Field("product") product: Int,
        @Field("count") count: Int,
    ): Response<OrderCoinProduct>

    @GET("/product/{product_id}/comments")
    suspend fun getProductComments(
        @Path("product_id") product_id: Int,
        @Query("page") page: Int
    ): CommentsResponse


    @FormUrlEncoded
    @POST("/product/{product_id}/comments")
    suspend fun postProductComments(
        @Path("product_id") product_id: Int,
        @Field("first_name") firstName: String,
        @Field("body") commentBody: String
    ): Response<Comment>

    //region favourite

    @POST("/product/{product_id}/favorite")
    suspend fun addFav(
        @Path("product_id") product_id: Int
    ): Response<Any>

    @DELETE("/product/{product_id}/favorite")
    suspend fun removeFav(
        @Path("product_id") product_id: Int
    ): Response<Any>
    //endregion

    //endregion

    //region News
    @GET("/posts")
    suspend fun getNews(
        @Query("page") page: Int
    ): NewsResponse

    @GET("/posts/{news_id}")
    suspend fun showNews(
        @Path("news_id") news_id: Int,
    ): Response<Post>
    //endregion

    //region Profile
    @GET("/profile")
    suspend fun getProfile(
    ): Response<ProfileResponse>

    @GET("/profile/coins")
    suspend fun getCoins(
    ): Response<Int>

    @FormUrlEncoded
    @PUT("/profile")
    suspend fun updateProfile(
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("inn") inn: String,
        @Field("name") name: String,
        @Field("region_id") region: Int,
    ): Response<ProfileResponse>


    @GET("/profile/favorites")
    suspend fun getFavProduct(
        @Query("page") page: Int,
        @QueryMap queryMap: Map<String, String>
    ): CategoryProductsResponse


    @GET("/profile/addresses")
    suspend fun getAddresses(
        @Query("page") page: Int
    ): AddressResponse

    @FormUrlEncoded
    @POST("/profile/addresses")
    suspend fun addAddress(
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("city") city: String,
        @Field("region") region: String,
        @Field("location[lat]") lat: Double,
        @Field("location[lng]") lng: Double,
        @Field("phone") phone: Long,
        @Field("region_id") regionId: Int,
        @Field("city_id") cityId: Int,
    ): Response<Address>


    @GET("/profile/addresses/{id}")
    suspend fun showAddress(
        @Path("id") id: Int
    ): Response<Address>

    @DELETE("/profile/addresses/{id}")
    suspend fun deleteAddress(
        @Path("id") id: Int
    ): Response<Any>

    @FormUrlEncoded
    @PUT("/profile/addresses/{id}")
    suspend fun updateAddress(
        @Path("id") id: Int,
        @Field("name") name: String,
        @Field("address") address: String,
        @Field("city") city: String,
        @Field("region") region: String,
        @Field("location[lat]") lat: Double,
        @Field("location[lng]") lng: Double,
        @Field("phone") phone: Long,
        @Field("region_id") regionId: Int,
        @Field("city_id") cityId: Int,
    ): Response<Address>


    @GET("/region/")
    suspend fun getRegions(): Response<List<Region>>

    //endregion

    //region cart
    @FormUrlEncoded
    @POST("/cart")
    suspend fun getCart(
        @FieldMap products: HashMap<String, Int>,
    ): Response<CartResponse>


    @GET("/cart/all")
    suspend fun getCartAll(): Response<CartResponse>

    @FormUrlEncoded
    @POST("/cart/store")
    suspend fun storeCart(
        @Field("product_id") productId: Int,
        @Field("count") count: Int,
    ): Response<Any>

    @FormUrlEncoded
    @POST("/cart/update")
    suspend fun updateCart(
        @Field("product_id") productId: Int,
        @Field("count") count: Int,
        @Field("_method") method: String = "PUT",
    ): Response<Any>

    @FormUrlEncoded
    @POST("/cart/delete")
    suspend fun deleteCart(
        @Field("product_id") productId: Int,
        @Field("_method") method: String = "DELETE",
    ): Response<Any>

    @DELETE("/cart/clear")
    suspend fun clearCart(): Response<Any>
    //endregion

    //region checkout
    @FormUrlEncoded
    @POST("/checkout")
    suspend fun checkout(
        @Field("payment_type") paymentType: String,
        @Field("address_id") addressId: Int? = null,
        @FieldMap products: HashMap<String, Int>,
        @FieldMap address: HashMap<String, String>? = null
    ): Response<Any>

    @FormUrlEncoded
    @POST("/checkout")
    suspend fun checkout(
        @Field("payment_type") paymentType: String,
        @Field("address_id") addressId: Int? = null,
        @FieldMap products: HashMap<String, Int>
    ): Response<Any>
    //endregion

    //region Orders
    //TODO:Connect Orders
    @GET("/orders")
    suspend fun getOrders(
        @Query("type") type: String,
        @Query("page") page: Int
    ): OrderResponse

    @GET("/orders/{order_id}")
    suspend fun showOrder(
        @Path("order_id") id: Int
    ): Response<Order>

    @DELETE("/orders/{order_id}")
    suspend fun cancelOrder(
        @Path("order_id") id: Int
    ): Response<Any>
    //endregion

    //region Search
    @GET("/search")
    suspend fun search(
        @Query("search") query: String? = null,
        @Query("article_number") query_article: String? = null,
        @Query("page") page: Int,
    ): CategoryProductsResponse
    //endregion


}