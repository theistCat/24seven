package uz.usoft.a24seven.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.models.VerifyResponse
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

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