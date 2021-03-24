package uz.usoft.a24seven.ui.news

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import uz.usoft.a24seven.network.SevenApi
import uz.usoft.a24seven.network.models.Post
import uz.usoft.a24seven.network.utils.NoConnectivityException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class NewsPagingSource(
    private val api: SevenApi,
) : PagingSource<Int, Post>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = api.getNews(position)
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

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return 0
    }

}