package uz.usoft.a24seven.ui.checkout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.usoft.a24seven.network.utils.Event
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.repository.SevenRepository

class CheckoutViewModel constructor(val repository: SevenRepository) : ViewModel()
{
    val checkoutResponse = MutableLiveData<Event<Resource<Any>>>()

    fun checkout(paymentType: String,addressId: Int,products: HashMap<String,Int>) {
        viewModelScope.launch {
            repository.checkout(paymentType, addressId, products).onEach {
                checkoutResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }
}