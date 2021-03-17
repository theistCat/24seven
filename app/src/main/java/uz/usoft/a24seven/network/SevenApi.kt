package uz.usoft.a24seven.network

import retrofit2.Response
import retrofit2.http.GET

interface SevenApi {
    @GET("/")
    suspend fun getHome(): Response<Any>
}