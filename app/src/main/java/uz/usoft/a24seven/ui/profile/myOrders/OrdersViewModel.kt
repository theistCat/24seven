package uz.usoft.a24seven.ui.profile.myOrders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import uz.usoft.a24seven.network.models.Order
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.repository.SevenRepository

class OrdersViewModel constructor(private val repository: SevenRepository) : ViewModel()
{
    fun getOrders(ordersType:String): Flow<PagingData<Order>> {
        return try {
            repository.getOrders(ordersType)
                .cachedIn(viewModelScope) }
        catch (e: NoConnectivityException) {
            throw e
        }
    }
}
