package a24seven.uz.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import a24seven.uz.network.models.VerifyResponse
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.Resource
import a24seven.uz.repository.SevenRepository

class AuthViewModel constructor(private val repository: SevenRepository) : ViewModel()
{
    val firstStepResponse = MutableLiveData<Event<Resource<Any>>>()
    val verifyResponse = MutableLiveData<Event<Resource<VerifyResponse>>>()

    fun getVerifyResponse(phone:String,code:String) {
        viewModelScope.launch {
            repository.verify(phone, code).onEach {
                verifyResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun getFirstStepResponse(phone:String) {
        viewModelScope.launch {
            repository.authFirstStep(phone).onEach {
                firstStepResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }
}