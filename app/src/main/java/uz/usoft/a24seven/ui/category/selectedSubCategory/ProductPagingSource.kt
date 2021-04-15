package uz.usoft.a24seven.ui.category.selectedSubCategory

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import uz.usoft.a24seven.network.SevenApi
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.network.utils.NoConnectivityException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class ProductPagingSource(
    private val api: SevenApi,
    private val categoryId: Int?=null,
    private val orderBy: String="popular",
    private val getFav:Boolean=false,
    private val isSearch:Boolean=false,
    private val query:String="",
) : PagingSource<Int, Product>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = when {
                getFav -> api.getCategoryProducts(categoryId ?: 0, position, orderBy)
                isSearch -> api.search(query,position,orderBy)
                else -> api.getFavProduct(position)
            }
            val feedPost = response.items

            LoadResult.Page(
                data = feedPost,
                prevKey = response.pagination.previous,
                nextKey = response.pagination.next,
            )
        } catch (exception: NoConnectivityException) {
            return LoadResult.Error(exception)
        }catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return 0
    }

}