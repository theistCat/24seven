package uz.usoft.a24seven.ui.profile.myOrders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.models.HomeResponse
import uz.usoft.a24seven.network.models.Order
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class OrdersViewModel constructor(private val repository: SevenRepository) : ViewModel()
{
    val showOrder = MutableLiveData<Event<Resource<Order>>>()

    fun showOrder(orderId:Int) {
        viewModelScope.launch {
            repository.showOrder(orderId).onEach {
                showOrder.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun getOrders(ordersType:String): Flow<PagingData<Order>> {
        return try {
            repository.getOrders(ordersType)
                .cachedIn(viewModelScope) }
        catch (e: NoConnectivityException) {
            throw e
        }
    }
}
