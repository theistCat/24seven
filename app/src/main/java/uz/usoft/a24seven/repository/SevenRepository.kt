package uz.usoft.a24seven.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.usoft.a24seven.network.SevenApi
import uz.usoft.a24seven.network.models.CategoryProductsItems
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.network.utils.safeApiCall
import uz.usoft.a24seven.ui.category.CategoryPagingSource

class SevenRepository(private val api: SevenApi) {
    val COMMENT_PAGING_SIZE = 8
    suspend fun getHome() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getHome() })
    }

    suspend fun getCategories() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getCategories() })
    }

    fun getCategoryProducts(
        categoryId: Int,
        orderBy: String
    ): Flow<PagingData<CategoryProductsItems>> {
        return Pager(
            config = PagingConfig(
                pageSize = COMMENT_PAGING_SIZE
            ),
            pagingSourceFactory = { CategoryPagingSource(api, categoryId, orderBy) }
        ).flow
    }
}