package uz.usoft.a24seven.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.models.ProfileResponse
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class ProfileViewModel constructor(private val repository: SevenRepository) : ViewModel()
{

    val logoutResponse = MutableLiveData<Event<Resource<Any>>>()
    val profileResponse = MutableLiveData<Event<Resource<ProfileResponse>>>()
    val updateResponse = MutableLiveData<Event<Resource<ProfileResponse>>>()

    fun getLogoutResponse() {
        viewModelScope.launch {
            repository.logout().onEach {
                logoutResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun getProfileResponse() {
        viewModelScope.launch {
            repository.getProfile().onEach {
                profileResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }


    fun getUpdateProfileResponse(firstName:String,lastName:String,dob:String,gender:Int) {
        viewModelScope.launch {
            repository.updateProfile(firstName, lastName, dob, gender).onEach {
                updateResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }
}