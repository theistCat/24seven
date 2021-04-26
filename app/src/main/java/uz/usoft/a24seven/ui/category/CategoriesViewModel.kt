package uz.usoft.a24seven.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.models.CategoryObject
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class CategoriesViewModel constructor(private val repository: SevenRepository) : ViewModel() {
    val getCategoriesResponse = MutableLiveData<Event<Resource<List<CategoryObject>>>>()

    fun getCategoriesResponse() {
        viewModelScope.launch {
            repository.getCategories().onEach {
                getCategoriesResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

//    fun getCategoryProducts(
//        categoryId: Int,
//        orderBy: String
//    ): Flow<PagingData<Product>> {
//        return repository.getCategoryProducts(categoryId, orderBy)
//            .cachedIn(viewModelScope)
//    }
}