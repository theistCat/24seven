package uz.usoft.a24seven.ui.seach

import android.app.DownloadManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import uz.usoft.a24seven.network.models.Product
import uz.usoft.a24seven.network.utils.NoConnectivityException
import uz.usoft.a24seven.repository.SevenRepository

class SearchViewModel constructor(private val repository: SevenRepository) : ViewModel()
{
    fun getSearchResponse(query: String): Flow<PagingData<Product>> {
        return try {
            repository.search(query)
                .cachedIn(viewModelScope) }
        catch (e: NoConnectivityException) {
            throw e
        }
    }

}