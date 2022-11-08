package a24seven.uz.ui.seach

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import a24seven.uz.network.models.CartItem
import a24seven.uz.network.models.Product
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.network.utils.Resource
import a24seven.uz.repository.SevenRepository

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