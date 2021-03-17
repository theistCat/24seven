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
import uz.usoft.a24seven.network.models.CategoriesResponse
import uz.usoft.a24seven.network.models.CategoryObject
import uz.usoft.a24seven.network.models.CategoryProductsItems
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class CategoryViewModel(private val repository: SevenRepository) : ViewModel() {
    val getCategoriesResponse = MutableLiveData<Event<Resource<CategoriesResponse>>>()

    fun getCategories() {
        viewModelScope.launch {
            repository.getCategories().onEach {
                getCategoriesResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun getCategoryProducts(
        categoryId: Int,
        orderBy: String
    ): Flow<PagingData<CategoryProductsItems>> {
        return repository.getCategoryProducts(categoryId, orderBy)
            .cachedIn(viewModelScope)
    }
}