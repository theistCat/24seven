package a24seven.uz.ui.profile.myOrders



import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import a24seven.uz.network.SevenApi
import a24seven.uz.network.models.Order
import a24seven.uz.network.utils.NoConnectivityException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1


class OrderPagingSource(
    private val api: SevenApi,
    private val ordersType:String,
) : PagingSource<Int, Order>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = api.getOrders(ordersType, position)
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

    override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
        return null
    }


}