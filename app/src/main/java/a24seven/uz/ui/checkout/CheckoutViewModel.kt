package a24seven.uz.ui.checkout

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import a24seven.uz.network.models.Region
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.Resource
import a24seven.uz.repository.SevenRepository

class CheckoutViewModel constructor(val repository: SevenRepository) : ViewModel()
{
    val checkoutResponse = MutableLiveData<Event<Resource<Any>>>()

    fun checkout(paymentType: String,addressId: Int?=null,products: HashMap<String,Int>,address: HashMap<String,String>?=null) {
        viewModelScope.launch {
            repository.checkout(paymentType, addressId, products, address).onEach {
                checkoutResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    val updateCartResponse = MutableLiveData<Event<Resource<Any>>>()

    fun emptyTheCart() = viewModelScope.launch {

        Log.d("update","inviewmodel")
        updateCartResponse.value= Event(Resource.Loading)
        repository.emptyTheCart().onCompletion {
            Log.d("update","inviewmodel completed")
            updateCartResponse.value= Event(Resource.Success(object :Any() { val data="Success"}))
        }.launchIn(viewModelScope)
    }


    val regionsResponse = MutableLiveData<Event<Resource<List<Region>>>>()
    fun getRegions() {
        viewModelScope.launch {
            repository.getRegions().onEach {
                regionsResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

}