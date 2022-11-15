package a24seven.uz.repository

import a24seven.uz.network.models.Comment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import a24seven.uz.network.CartDao
import a24seven.uz.network.SevenApi
import a24seven.uz.network.models.Address
import a24seven.uz.network.models.Characteristics
import a24seven.uz.network.models.Order
import a24seven.uz.network.models.Post
import a24seven.uz.network.models.Product
import a24seven.uz.network.models.*
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.network.utils.Resource
import a24seven.uz.network.utils.safeApiCall
import a24seven.uz.ui.category.selectedSubCategory.ProductPagingSource
import a24seven.uz.ui.news.NewsPagingSource
import a24seven.uz.ui.products.CommentsPagingSource
import a24seven.uz.ui.profile.myAddresses.AddressPagingSource
import a24seven.uz.ui.profile.myOrders.OrderPagingSource

class SevenRepository(private val api: SevenApi, private val cartDao: CartDao) {

    companion object {
        private const val PRODUCT_PAGING_SIZE = 5
        private const val COMMENT_PAGING_SIZE = 5
        private const val ADDRESS_PAGING_SIZE = 5
        private const val NEWS_PAGING_SIZE = 5
        private const val ORDERS_PAGING_SIZE = 999
    }

    //region Auth
    suspend fun verify(phone: String, code: String) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.verifyCode(phone, code) })
    }

    suspend fun authFirstStep(phone: String) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.authFirstStep(phone) })
    }

    suspend fun logout() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.logout() })
    }
//endregion

    //region Profile
    suspend fun getProfile() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getProfile() })
    }

    suspend fun getCoins() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getCoins() })
    }

    suspend fun getRegions() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getRegions() })
    }

    suspend fun updateProfile(
        firstName: String,
        lastName: String,
        inn: String,
        name: String,
        region_id: Int
    ) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.updateProfile(firstName, lastName, inn, name, region_id) })
    }

    fun getFavProducts(orderBy: Map<String,String>): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = PRODUCT_PAGING_SIZE,
                enablePlaceholders = true,
                maxSize = 50
            ),
            pagingSourceFactory = {
                try {
                    ProductPagingSource(api, orderBy = orderBy, getFav = true)
                } catch (e: NoConnectivityException) {
                    throw  e
                }
            }
        ).flow

    }

    fun getAddresses(): Flow<PagingData<Address>> {
        return Pager(
            config = PagingConfig(
                pageSize = ADDRESS_PAGING_SIZE,
                enablePlaceholders = true,
                maxSize = 50
            ),
            pagingSourceFactory = {
                try {
                    AddressPagingSource(api)
                } catch (e: NoConnectivityException) {
                    throw  e
                }
            }
        ).flow
    }

    suspend fun addAddress(
        name: String,
        address: String,
        city: String,
        region: String,
        lat: Double,
        lng: Double,
        phone: Long,
        regionId: Int,
        cityId: Int
    ) = flow {
        emit(Resource.Loading)
        emit(safeApiCall {
            api.addAddress(
                name,
                address,
                city,
                region,
                lat,
                lng,
                phone,
                regionId,
                cityId
            )
        })
    }

    suspend fun updateAddress(
        id: Int,
        name: String,
        address: String,
        city: String,
        region: String,
        lat: Double,
        lng: Double,
        phone: Long,
        regionId: Int,
        cityId: Int
    ) = flow {
        emit(Resource.Loading)
        emit(safeApiCall {
            api.updateAddress(
                id,
                name,
                address,
                city,
                region,
                lat,
                lng,
                phone,
                regionId,
                cityId
            )
        })
    }

    suspend fun showAddress(id: Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.showAddress(id) })
    }

    suspend fun deleteAddress(id: Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.deleteAddress(id) })
    }
    //endregion

    //region Home
    suspend fun getHome() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getHome() })
    }
    //endregion

    //region Categories
    suspend fun getCategories() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getCategories() })
    }


    fun getCategoryProducts(
        categoryId: Int,
        orderBy: Map<String,String>,
        characteristics: MutableLiveData<List<Characteristics>>
    ): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = PRODUCT_PAGING_SIZE,
                enablePlaceholders = true,
                maxSize = 50
            ),
            pagingSourceFactory = {
                try {
                    ProductPagingSource(api, categoryId, orderBy, characteristics = characteristics)
                } catch (e: NoConnectivityException) {
                    throw  e
                }
            }
        ).flow

    }

    fun getFilteredCategoryProducts(
        categoryId: Int,
        orderBy: Map<String,String>,
        characteristics: MutableLiveData<List<Characteristics>>,
        filterOptions: HashMap<String, String>
    ): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = PRODUCT_PAGING_SIZE,
                enablePlaceholders = true,
                maxSize = 50
            ),
            pagingSourceFactory = {
                try {
                    ProductPagingSource(
                        api,
                        categoryId,
                        orderBy,
                        characteristics = characteristics,
                        isFilter = true,
                        filterOptions = filterOptions
                    )
                } catch (e: NoConnectivityException) {
                    throw  e
                }
            }
        ).flow

    }
    //endregion

    //region Product

    suspend fun getProduct(productID: Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getProduct(productID) })
    }

    suspend fun getCoinProduct(productID: Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getProduct(productID) })
    }

    suspend fun getCoinProducts() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getCoinProducts() })
    }

    suspend fun orderCoinProducts(productId: Int, count: Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.orderCoins(productId, count) })
    }

    fun getProductComments(
        productID: Int,
    ): Flow<PagingData<Comment>> {
        return Pager(
            config = PagingConfig(
                pageSize = COMMENT_PAGING_SIZE,
                enablePlaceholders = true,
                maxSize = 50
            ),
            pagingSourceFactory = { CommentsPagingSource(api, productID) }
        ).flow

    }

    suspend fun addComment(productID: Int, firstName: String, commentBody: String) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.postProductComments(productID, firstName, commentBody) })
    }

    suspend fun addFav(productID: Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.addFav(productID) })
    }

    suspend fun removeFav(productID: Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.removeFav(productID) })
    }

    //endregion

    //region News
    fun getNews(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = NEWS_PAGING_SIZE
            ),
            pagingSourceFactory = { NewsPagingSource(api) }
        ).flow
    }

    suspend fun showNews(newsId: Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.showNews(newsId) })
    }
    //endregion

    //region cart
    suspend fun getCart(products: HashMap<String, Int>) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getCartAll() })
    }
    //endregion


    //region cart database
    val cart: Flow<List<CartItem>> = cartDao.getAll()


    suspend fun storeCart(cartItem: CartItem) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.storeCart(cartItem.id, cartItem.count) })
    }


    suspend fun deleteCart(cartItem: CartItem) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.deleteCart(cartItem.id) })
    }

    suspend fun updateCart(cartItem: CartItem) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.updateCart(cartItem.id, cartItem.count) })
    }


    suspend fun addToCartWithoutEmit(cartItem: CartItem): Long {
        return cartDao.insert(cartItem)
    }

    suspend fun getItem(id: Int): CartItem {
        return cartDao.getItem(id)
    }

    suspend fun addToCartReplace(cartItem: CartItem): Long {
        return cartDao.insertReplace(cartItem)
    }

//    suspend fun delete(cartItem: CartItem) = flow{
//
//        Log.d("remove","inrepository")
//        emit(Resource.Loading)
//        emit(cartDao.delete(cartItem))
//    }

    suspend fun delete(cartItem: CartItem) = flow {

        emit(Resource.Loading)
        emit(safeApiCall { api.deleteCart(cartItem.id) })
    }

    suspend fun deleteW(cartItem: CartItem): Int {

        return cartDao.delete(cartItem)
    }


    suspend fun update(cartItem: CartItem) = flow {

        Log.d("remove", "inrepository")
        emit(Resource.Loading)
        emit(cartDao.updateCartItem(cartItem))
    }

    suspend fun updateWithoutEmit(cartItem: CartItem): Int {
        return cartDao.updateCartItem(cartItem)
    }

    suspend fun emptyTheCart() = flow {

        Log.d("remove", "inrepository")
        emit(Resource.Loading)
        emit(cartDao.emptyTheCart())
    }
    //endregion


    //region Checkout
    suspend fun checkout(
        paymentType: String,
        addressId: Int? = null,
        products: HashMap<String, Int>,
        address: HashMap<String, String>? = null
    ) = flow {
        emit(Resource.Loading)
        if (address != null)
            emit(safeApiCall {
                api.checkout(
                    paymentType,
                    addressId,
                    products = products,
                    address = address
                )
            })
        else
            emit(safeApiCall { api.checkout(paymentType, addressId, products = products) })
    }
    //endregion


    //region Orders
    fun getOrders(orderType: String): Flow<PagingData<Order>> {
        return Pager(
            config = PagingConfig(
                pageSize = ORDERS_PAGING_SIZE
            ),
            pagingSourceFactory = { OrderPagingSource(api, orderType) }
        ).flow

    }

    suspend fun showOrder(orderId: Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.showOrder(orderId) })
    }

    suspend fun cancelOrder(orderId: Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.cancelOrder(orderId) })
    }
    //endregion

    //region Search

    fun search(query: String): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = PRODUCT_PAGING_SIZE
            ),
            initialKey = 1,
            pagingSourceFactory = { ProductPagingSource(api, isSearch = true, query = query) }
        ).flow

    }

    //endregion
}