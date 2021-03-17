package uz.usoft.a24seven.ui.category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.models.CategoryObject
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class CategoriesViewModel constructor(private val repository: SevenRepository) : ViewModel() {
    val getCategoriesResponse = MutableLiveData<Event<Resource<List<CategoryObject>>>>()

    fun getCategoriesResponse(){
        viewModelScope.launch {
            repository.getCategories().onEach{
                getCategoriesResponse.value= Event(it)
            }.launchIn(viewModelScope)
        }
    }
}