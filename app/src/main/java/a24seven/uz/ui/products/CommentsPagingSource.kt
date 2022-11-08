package a24seven.uz.ui.products


import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import a24seven.uz.network.SevenApi
import a24seven.uz.network.models.Comment
import a24seven.uz.network.utils.NoConnectivityException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class CommentsPagingSource(
    private val api: SevenApi,
    private val productId: Int
) : PagingSource<Int, Comment>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = api.getProductComments(productId, position)
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

    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return 0
    }

}