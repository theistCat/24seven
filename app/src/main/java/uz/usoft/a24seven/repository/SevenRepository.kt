package uz.usoft.a24seven.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.usoft.a24seven.network.SevenApi
import uz.usoft.a24seven.network.models.Comment
import uz.usoft.a24seven.network.models.Post
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.network.utils.safeApiCall
import uz.usoft.a24seven.ui.category.selectedSubCategory.ProductPagingSource
import uz.usoft.a24seven.ui.news.NewsPagingSource
import uz.usoft.a24seven.ui.products.CommentsPagingSource

class SevenRepository(private val api: SevenApi) {
    private val PRODUCT_PAGING_SIZE = 5
    private val COMMENT_PAGING_SIZE = 5
    private val NEWS_PAGING_SIZE = 5

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

    suspend fun getProfile() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getProfile() })
    }


    suspend fun getHome() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getHome() })
    }

    suspend fun getCategories() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getCategories() })
    }

    suspend fun getProduct(productID:Int) = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getProduct(productID) })
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
}