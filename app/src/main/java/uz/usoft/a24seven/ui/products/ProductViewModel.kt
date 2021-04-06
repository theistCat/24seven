package uz.usoft.a24seven.ui.products

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.models.Comment
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class ProductViewModel constructor(private val repository: SevenRepository) : ViewModel(){

    fun getProductsResponse(categoryId: Int,
                            orderBy: String): Flow<PagingData<Product>> {
        return try {
         repository.getCategoryProducts(categoryId, orderBy)
            .cachedIn(viewModelScope) }
        catch (e:NoConnectivityException) {
            throw e
        }
    }


    fun getProductCommentsResponse(productId: Int): Flow<PagingData<Comment>> {
        return repository.getProductComments(productId)
            .cachedIn(viewModelScope)
    }

    val getProductResponse = MutableLiveData<Event<Resource<Product>>>()

    fun getProduct(productId:Int) {
        viewModelScope.launch {
            repository.getProduct(productId).onEach {
                getProductResponse.value = Event(it)
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

}