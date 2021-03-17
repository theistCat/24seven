package uz.usoft.a24seven.ui.category

import androidx.paging.PagingSource
import retrofit2.HttpException
import uz.usoft.a24seven.network.SevenApi
import uz.usoft.a24seven.network.models.CategoryProductsItems
import uz.usoft.a24seven.network.models.CategoryProductsResponse
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class CategoryPagingSource(
    private val api: SevenApi,
    private val categoryId: Int,
    private val orderBy: String
) : PagingSource<Int, CategoryProductsItems>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CategoryProductsItems> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = api.getCategoryProducts(categoryId, position, orderBy)
            val feedPost = response.items

            LoadResult.Page(
                data = feedPost,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (response.pagination.current!! >= response.pagination.next!!) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}