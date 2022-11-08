package a24seven.uz.ui.coin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import a24seven.uz.network.models.CoinProduct
import a24seven.uz.network.models.OrderCoinProduct
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.Resource
import a24seven.uz.repository.SevenRepository

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