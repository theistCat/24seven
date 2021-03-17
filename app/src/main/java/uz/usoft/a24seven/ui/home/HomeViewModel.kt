package uz.usoft.a24seven.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class HomeViewModel constructor(private val repository: SevenRepository) : ViewModel() {

    val getHomeResponse = MutableLiveData<Event<Resource<Any>>>()

    fun getHome(){
        viewModelScope.launch {
            repository.getHome().onEach{
                getHomeResponse.value= Event(it)
            }.launchIn(viewModelScope)
        }
    }
}