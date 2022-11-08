package a24seven.uz

import a24seven.uz.network.models.CartItem
import androidx.lifecycle.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.Resource
import a24seven.uz.repository.SevenRepository


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
