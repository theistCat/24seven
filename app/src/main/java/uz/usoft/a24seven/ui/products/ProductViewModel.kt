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
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class ProductViewModel constructor(private val repository: SevenRepository) : ViewModel(){

    fun getProductsResponse(categoryId: Int,
                            orderBy: String): Flow<PagingData<Product>> {
        return repository.getCategoryProducts(categoryId, orderBy)
            .cachedIn(viewModelScope)
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

}