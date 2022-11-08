package a24seven.uz.ui.profile.myOrders

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import a24seven.uz.network.models.Order
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.network.utils.Resource
import a24seven.uz.repository.SevenRepository

class OrdersViewModel(private val repository: SevenRepository) : ViewModel() {
    val showOrder = MutableLiveData<Event<Resource<Order>>>()

    fun showOrder(orderId: Int) {
        viewModelScope.launch {
            repository.showOrder(orderId).onEach {
                showOrder.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    val cancelOrder = MutableLiveData<Event<Resource<Any>>>()

    fun cancelOrder(orderId: Int) {
        viewModelScope.launch {
            repository.cancelOrder(orderId).onEach {
                cancelOrder.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun getOrders(ordersType: String): Flow<PagingData<Order>> {
        return try {
            repository.getOrders(ordersType)
                .cachedIn(viewModelScope)
        } catch (e: NoConnectivityException) {
            throw e
        }
    }

    var dateListLive2: MutableList<String> = mutableListOf()


    fun setDateList2(price: String) {
        dateListLive2.add(price)
    }


    var priceListLive2: MutableList<Int> = mutableListOf()


    fun setPriceList2(price: Int) {
        priceListLive2.add(price)
    }

    var dateListLive1: MutableList<String> = mutableListOf()


    fun setDateList1(price: String) {
        dateListLive1.add(price)
    }


    var priceListLive1: MutableList<Int> = mutableListOf()


    fun setPriceList1(price: Int) {
        priceListLive1.add(price)
    }


    var dateTo = 0L
    var dateFrom = 0L

    var sumCount = 0
    var millis = 0L

    fun addThree(it: Long) {
        millis = it
    }

    fun addTwo(it: Int) {
        sumCount += it
    }

//    var date = ""
//
//    fun dates(it: String) {
//        date += it
//    }

//    private var ordersPriceList = Int
//    val ordersPriceList: Int.Companion = _ordersPriceList
//
//    fun setOrders(price: Int.Companion) {
//        _ordersPriceList = price
//    }
}
