package uz.usoft.a24seven.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class ProfileViewModel constructor(val repository: SevenRepository) : ViewModel()
{

    val logoutResponse = MutableLiveData<Event<Resource<Any>>>()

    fun getLogoutResponse() {
        viewModelScope.launch {
            repository.logout().onEach {
                logoutResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

}