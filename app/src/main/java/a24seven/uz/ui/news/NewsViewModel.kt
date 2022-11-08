package a24seven.uz.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import a24seven.uz.network.models.Post
import a24seven.uz.network.utils.Event
import a24seven.uz.network.utils.Resource
import a24seven.uz.repository.SevenRepository

class NewsViewModel constructor(private val repository: SevenRepository) : ViewModel(){


    fun getNews(): Flow<PagingData<Post>> {
        return repository.getNews()
            .cachedIn(viewModelScope)
    }

    val showNewsResponse = MutableLiveData<Event<Resource<Post>>>()

    fun showNews(newsId:Int) {
        viewModelScope.launch {
            repository.showNews(newsId).onEach {
                showNewsResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }
}