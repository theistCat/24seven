package uz.usoft.a24seven.ui.home

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.android.gms.common.api.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.R
import uz.usoft.a24seven.network.models.CartItem
import uz.usoft.a24seven.network.models.HomeResponse
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class HomeViewModel constructor(private val repository: SevenRepository) : ViewModel() {

    val getHomeResponse = MutableLiveData<Event<Resource<HomeResponse>>>()

    fun getHome() {
        viewModelScope.launch {
            repository.getHome().onEach {
                getHomeResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }


    val favResponse = MutableLiveData<Event<Resource<Any>>>()


    fun addFav(productId:Int) {
        viewModelScope.launch {
            repository.addFav(productId).onEach {
                favResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun removeFav(productId:Int) {
        viewModelScope.launch {
            repository.removeFav(productId).onEach {
                favResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

//
//    val addToCartResponse = MutableLiveData<Long>()
//    fun addToCart(item: CartItem) = viewModelScope.launch {
//
//        addToCartResponse.value=repository.addToCartWithoutEmit(item)
//
//    }

    val addToCartResponse = MutableLiveData<Event<Resource<Any>>>()
    fun storeCart(item: CartItem) = viewModelScope.launch {

        viewModelScope.launch {
            repository.storeCart(item).onEach {
                addToCartResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }

    }


    fun delete(item: CartItem) =
        viewModelScope.launch {
        repository.delete(item)
    }

//    val removeCart = MutableLiveData<Event<Resource<Any>>>()
//
//
//    fun delete(item: CartItem) {
//        viewModelScope.launch {
//            repository.delete(item).onEach {
//                removeCart.value = Event(it)
//            }.launchIn(viewModelScope)
//        }
//    }


}