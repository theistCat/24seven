package uz.usoft.a24seven.ui.cart

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.models.CartItem
import uz.usoft.a24seven.network.models.CartResponse
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class CartViewModel constructor(private val repository: SevenRepository) : ViewModel(){


    val cart: LiveData<List<CartItem>> = repository.cart.asLiveData()

    val removeFromCartResponse = MutableLiveData<Event<Resource<Any>>>()
    val updateCartResponse = MutableLiveData<Event<Resource<Any>>>()

    val cartResponse= MutableLiveData<Event<Resource<CartResponse>>>()

    fun remove(item: CartItem) = viewModelScope.launch {

        Log.d("removeFromCartResponse","inviewmodel")
        removeFromCartResponse.value= Event(Resource.Loading)
        repository.delete(item).onCompletion {
            Log.d("removeFromCartResponse","inviewmodel completed")
            removeFromCartResponse.value= Event(Resource.Success(object :Any() { val data="Success"}))
        }.launchIn(viewModelScope)
    }

    fun update(item: CartItem) = viewModelScope.launch {

        Log.d("update","inviewmodel")
        updateCartResponse.value= Event(Resource.Loading)
        repository.update(item).onCompletion {
            Log.d("update","inviewmodel completed")
            updateCartResponse.value= Event(Resource.Success(object :Any() { val data="Success"}))
        }.launchIn(viewModelScope)
    }

    fun getCart(products: HashMap<String,Int>) {
        viewModelScope.launch {
            repository.getCart(products).onEach {
                cartResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }
}