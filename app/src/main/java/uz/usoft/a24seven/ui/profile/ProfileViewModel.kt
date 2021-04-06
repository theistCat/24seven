package uz.usoft.a24seven.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.network.models.ProfileResponse
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class ProfileViewModel constructor(private val repository: SevenRepository) : ViewModel()
{

    val logoutResponse = MutableLiveData<Event<Resource<Any>>>()
    val profileResponse = MutableLiveData<Event<Resource<ProfileResponse>>>()
    val updateResponse = MutableLiveData<Event<Resource<ProfileResponse>>>()


    val favResponse = MutableLiveData<Event<Resource<Any>>>()

    fun getFavProductsResponse(): Flow<PagingData<Product>> {
        return try {
            repository.getFavProducts()
                .cachedIn(viewModelScope) }
        catch (e: NoConnectivityException) {
            throw e
        }
    }

    fun addFav(productId:Int) {
        viewModelScope.launch {
            repository.addFav(productId).onEach {
                favResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun removeFav(productId:Int) {
        viewModelScope.launch {
            repository.removeFav(productId).onEach {
                favResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

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