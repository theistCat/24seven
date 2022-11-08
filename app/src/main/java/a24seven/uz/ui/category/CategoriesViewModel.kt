package a24seven.uz.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import a24seven.uz.network.models.CategoryObject
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.Resource
import a24seven.uz.repository.SevenRepository

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