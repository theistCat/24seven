package uz.usoft.a24seven.ui.seach

import android.app.DownloadManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.models.CartItem
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class SearchViewModel constructor(private val repository: SevenRepository) : ViewModel()
{
    fun getSearchResponse(query: String): Flow<PagingData<Product>> {
        return try {
            repository.search(query)
                .cachedIn(viewModelScope) }
        catch (e: NoConnectivityException) {
            throw e
        }
    }


    val addToCartResponse = MutableLiveData<Long>()
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

    fun addToCart(item: CartItem) = viewModelScope.launch {
        addToCartResponse.value=repository.addToCartWithoutEmit(item)
    }
}