package a24seven.uz.ui.cart

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import a24seven.uz.network.models.CartItem
import a24seven.uz.network.models.CartResponse
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.Resource
import a24seven.uz.repository.SevenRepository

class CartViewModel constructor(private val repository: SevenRepository) : ViewModel(){


    val cart: LiveData<List<CartItem>> = repository.cart.asLiveData()

    val removeFromCartResponse = MutableLiveData<Int>()
    val updateCartResponse = MutableLiveData<Event<Resource<Any>>>()

    val cartResponse= MutableLiveData<Event<Resource<CartResponse>>>()

//    fun remove(item: CartItem) = viewModelScope.launch {
//            removeFromCartResponse.value= repository.deleteW(item)
//    }

//    fun remove(item: CartItem) = viewModelScope.launch {
//        removeFromCartResponse.value= repository.deleteW(item)
//    }

    val removeCart = MutableLiveData<Event<Resource<Any>>>()


    fun remove(item: CartItem) {
        viewModelScope.launch {
            repository.delete(item).onEach {
                removeCart.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun update(item: CartItem) = viewModelScope.launch {
        Log.d("update","inviewmodel")
        updateCartResponse.value= Event(Resource.Loading)
        repository.update(item).onCompletion {
            Log.d("update","inviewmodel completed")
            updateCartResponse.value= Event(Resource.Success(object :Any() { val data="Success"}))
        }.launchIn(viewModelScope)
    }

    fun updateCart(item: CartItem) = viewModelScope.launch {
        viewModelScope.launch {
            repository.updateCart(item).onEach {
                updateCartResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun emptyTheCart() = viewModelScope.launch {

        Log.d("update","inviewmodel")
        updateCartResponse.value= Event(Resource.Loading)
        repository.emptyTheCart().onCompletion {
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