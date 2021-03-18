package uz.usoft.a24seven.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.usoft.a24seven.network.SevenApi
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.network.utils.safeApiCall
import uz.usoft.a24seven.ui.category.selectedSubCategory.ProductPagingSource

class SevenRepository(private val api: SevenApi) {
    private val PRODUCT_PAGING_SIZE = 5

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
                pageSize = PRODUCT_PAGING_SIZE
            ),
            pagingSourceFactory = { ProductPagingSource(api, categoryId, orderBy) }
        ).flow
    }
}