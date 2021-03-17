package uz.usoft.a24seven.repository

import kotlinx.coroutines.flow.flow
import uz.usoft.a24seven.network.SevenApi
import uz.usoft.a24seven.network.utils.Resource
import uz.usoft.a24seven.network.utils.safeApiCall

class SevenRepository (private val api: SevenApi){

    suspend fun getHome() = flow {
        emit(Resource.Loading)
        emit(safeApiCall { api.getHome() })
    }
}