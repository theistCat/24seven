package a24seven.uz.ui.profile

import a24seven.uz.network.models.Address
import a24seven.uz.network.models.Product
import a24seven.uz.network.models.ProfileResponse
import a24seven.uz.network.models.Region
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import a24seven.uz.network.models.*
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.NoConnectivityException
import a24seven.uz.network.utils.Resource
import a24seven.uz.repository.SevenRepository

class ProfileViewModel constructor(private val repository: SevenRepository) : ViewModel() {

    val logoutResponse = MutableLiveData<Event<Resource<Any>>>()
    val profileResponse = MutableLiveData<Event<Resource<ProfileResponse>>>()
    val updateResponse = MutableLiveData<Event<Resource<ProfileResponse>>>()
    val addAddressResponse = MutableLiveData<Event<Resource<Address>>>()
    val updateAddressResponse = MutableLiveData<Event<Resource<Address>>>()
    val showAddressResponse = MutableLiveData<Event<Resource<Address>>>()
    val deleteAddressResponse = MutableLiveData<Event<Resource<Any>>>()
    val regionsResponse = MutableLiveData<Event<Resource<List<Region>>>>()

    var update: Boolean = false


    val favResponse = MutableLiveData<Event<Resource<Any>>>()

    fun getFavProductsResponse(orderBy: Map<String, String>): Flow<PagingData<Product>> {
        return try {
            repository.getFavProducts(orderBy)
                .cachedIn(viewModelScope)
        } catch (e: NoConnectivityException) {
            throw e
        }
    }

    fun addFav(productId: Int) {
        viewModelScope.launch {
            repository.addFav(productId).onEach {
                favResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun removeFav(productId: Int) {
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


    fun getUpdateProfileResponse(
        firstName: String,
        lastName: String,
        inn: Int,
        name: String,
        region_id: Int
    ) {
        viewModelScope.launch {
            repository.updateProfile(firstName, lastName, inn, name, region_id).onEach {
                updateResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun addAddress(
        name: String,
        address: String,
        city: String,
        region: String,
        lat: Double,
        lng: Double,
        phone: Long,
        regionId: Int,
        cityId: Int
    ) {
        viewModelScope.launch {
            repository.addAddress(name, address, city, region, lat, lng, phone, regionId, cityId)
                .onEach {
                    addAddressResponse.value = Event(it)
                }.launchIn(viewModelScope)
        }
    }

    fun updateAddress(
        id: Int,
        name: String,
        address: String,
        city: String,
        region: String,
        lat: Double,
        lng: Double,
        phone: Long,
        regionId: Int,
        cityId: Int
    ) {
        viewModelScope.launch {
            repository.updateAddress(
                id,
                name,
                address,
                city,
                region,
                lat,
                lng,
                phone,
                regionId,
                cityId
            ).onEach {
                updateAddressResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun showAddress(id: Int) {
        viewModelScope.launch {
            repository.showAddress(id).onEach {
                showAddressResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun deleteAddress(id: Int) {
        viewModelScope.launch {
            repository.deleteAddress(id).onEach {
                deleteAddressResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun getRegions() {
        viewModelScope.launch {
            repository.getRegions().onEach {
                regionsResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun getAddresses(): Flow<PagingData<Address>> {
        return try {
            repository.getAddresses()
                .cachedIn(viewModelScope)
        } catch (e: NoConnectivityException) {
            throw e
        }
    }

    val addToCartResponse = MutableLiveData<Long>()
    fun addToCart(item: CartItem) = viewModelScope.launch {

        addToCartResponse.value = repository.addToCartWithoutEmit(item)

    }
}