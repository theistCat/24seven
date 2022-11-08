package a24seven.uz.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import a24seven.uz.network.models.YandexGeoObject

interface YandexApi {
    @GET("1.x/")
    suspend fun getAddressData(
        @Query( "apikey") key: String,
        @Query("geocode") geocode: String,
        @Query("format") format:String="json",
    ): Response<YandexGeoObject>
}