package uz.usoft.a24seven.ui.checkout

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.models.Region
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

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