package uz.usoft.a24seven

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import uz.usoft.a24seven.network.models.CartItem
import uz.usoft.a24seven.repository.SevenRepository


class MainViewModel constructor(private val repository: SevenRepository) : ViewModel() {
    val cart: LiveData<List<CartItem>> = repository.cart.asLiveData()
}
