package uz.usoft.a24seven.ui.coin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.models.CoinProduct
import uz.usoft.a24seven.network.models.OrderCoinProduct
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class CoinViewModel  constructor(val repository: SevenRepository) : ViewModel()
{
    val getCoinProductsResponse = MutableLiveData<Event<Resource<List<CoinProduct>>>>()

    fun getCoinProducts() {
        viewModelScope.launch {
            repository.getCoinProducts().onEach {
                getCoinProductsResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    val getCoinResponse = MutableLiveData<Event<Resource<Int>>>()
    fun getCoins() {
        viewModelScope.launch {
            repository.getCoins().onEach {
                getCoinResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    val getOrderCoinResponse = MutableLiveData<Event<Resource<OrderCoinProduct>>>()
    fun orderCoins(productId:Int,count:Int) {
        viewModelScope.launch {
            repository.orderCoinProducts(productId, count).onEach {
                getOrderCoinResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }


}