package uz.usoft.a24seven

import androidx.lifecycle.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.models.CartItem
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository


class MainViewModel constructor(private val repository: SevenRepository) : ViewModel() {
    val cart: LiveData<List<CartItem>> = repository.cart.asLiveData()

    val getCoinResponse = MutableLiveData<Event<Resource<Int>>>()
    fun getCoins() {
        viewModelScope.launch {
            repository.getCoins().onEach {
                getCoinResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }
}
