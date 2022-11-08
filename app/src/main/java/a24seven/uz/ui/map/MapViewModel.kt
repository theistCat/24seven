package a24seven.uz.ui.map

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import a24seven.uz.network.YandexApi
import a24seven.uz.network.models.YandexGeoObject
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.Resource
import a24seven.uz.network.utils.safeApiCall

class MapViewModel constructor(): ViewModel() {

    val addressData = MutableLiveData<Event<Resource<YandexGeoObject>>>()
    val retrofit: Retrofit=Retrofit.Builder()
        .baseUrl("https://geocode-maps.yandex.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    var yandexApi: YandexApi =retrofit.create(YandexApi::class.java)

        suspend fun getAddressDataRepo(key:String,long:String,lat:String)= flow {
        emit(Resource.Loading)
        emit(safeApiCall { yandexApi.getAddressData(key,"$long,$lat") })
            Log.i("mapkitlocation",yandexApi.getAddressData(key,"$long,$lat").toString())
    }

    fun getAddressData(key:String,long:String,lat:String) {
        viewModelScope.launch {
            getAddressDataRepo(key, long, lat).onEach { addressData.value = Event(it) }.launchIn(viewModelScope)
        }
    }
}