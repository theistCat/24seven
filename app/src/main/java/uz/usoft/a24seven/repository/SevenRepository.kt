package uz.usoft.a24seven.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.usoft.a24seven.network.SevenApi
import uz.usoft.a24seven.network.models.Address
import uz.usoft.a24seven.network.models.Comment
import uz.usoft.a24seven.network.models.Post
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.network.utils.safeApiCall
import uz.usoft.a24seven.ui.category.selectedSubCategory.ProductPagingSource
import uz.usoft.a24seven.ui.news.NewsPagingSource
import uz.usoft.a24seven.ui.products.CommentsPagingSource
import uz.usoft.a24seven.ui.profile.myAddresses.AddressPagingSource

class SevenRepository(private val api: SevenApi) {
    private val PRODUCT_PAGING_SIZE = 5
    private val COMMENT_PAGING_SIZE = 5
    private val ADDRESS_PAGING_SIZE = 5
    private val NEWS_PAGING_SIZE = 5


    //region Auth
    suspend fun verify(phone:String,code:String) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.verifyCode(phone, code) })
    }

    suspend fun authFirstStep(phone:String) = flow {
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

    suspend fun updateProfile(firstName:String,lastName:String,dob:String,gender:Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.updateProfile(firstName, lastName, dob, gender) })
    }

    fun getFavProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = PRODUCT_PAGING_SIZE,
                enablePlaceholders = true,
                maxSize = 50
            ),
            pagingSourceFactory = { try{ ProductPagingSource(api,getFav = true) } catch (e:NoConnectivityException) { throw  e} }
        ).flow

    }

    fun getAddresses(): Flow<PagingData<Address>> {
        return Pager(
            config = PagingConfig(
                pageSize = ADDRESS_PAGING_SIZE,
                enablePlaceholders = true,
                maxSize = 50
            ),
            pagingSourceFactory = { try{ AddressPagingSource(api) }catch (e:NoConnectivityException) { throw  e} }
        ).flow
    }

    suspend fun addAddress(name:String,address:String,city:String,region:String,lat:Double,lng:Double,phone:String) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.addAddress(name, address, city, region, lat, lng, phone) })
    }

    suspend fun updateAddress(id:Int,name:String,address:String,city:String,region:String,lat:Double,lng:Double,phone:String) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.updateAddress(id,name, address, city, region, lat, lng, phone) })
    }

    suspend fun showAddress(id:Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.showAddress(id) })
    }

    suspend fun deleteAddress(id:Int) = flow {
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
        orderBy: String
    ): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = PRODUCT_PAGING_SIZE,
                enablePlaceholders = true,
                maxSize = 50
            ),
            pagingSourceFactory = { try{ProductPagingSource(api, categoryId, orderBy)}catch (e:NoConnectivityException) { throw  e} }
        ).flow
    }
    //endregion

    //region Product

    suspend fun getProduct(productID:Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getProduct(productID) })
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

    suspend fun addComment(productID:Int,firstName: String,commentBody:String) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.postProductComments(productID,firstName, commentBody) })
    }

    suspend fun addFav(productID:Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.addFav(productID) })
    }

    suspend fun removeFav(productID:Int) = flow {
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

    suspend fun showNews(newsId:Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.showNews(newsId) })
    }
    //endregion
}