package a24seven.uz.ui.products

import a24seven.uz.network.models.Characteristics
import a24seven.uz.network.models.Comment
import a24seven.uz.network.models.Product
import android.util.Log
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

class ProductViewModel constructor(private val repository: SevenRepository) : ViewModel(){


    val characteristics= MutableLiveData<List<Characteristics>>()

    fun getProductsResponse(categoryId: Int,
                            orderBy: Map<String,String>): Flow<PagingData<Product>> {
        return try {
         repository.getCategoryProducts(categoryId, orderBy,characteristics)
            .cachedIn(viewModelScope) }
        catch (e: NoConnectivityException) {
            throw e
        }
    }

    fun getFilteredProductsResponse(categoryId: Int,
                                    orderBy: Map<String,String>,
                                    filterOptions :HashMap<String,String>): Flow<PagingData<Product>> {
        return try {
            repository.getFilteredCategoryProducts(categoryId, orderBy,characteristics,filterOptions)
                .cachedIn(viewModelScope) }
        catch (e: NoConnectivityException) {
            throw e
        }
    }


    fun getProductCommentsResponse(productId: Int): Flow<PagingData<Comment>> {
        return repository.getProductComments(productId)
            .cachedIn(viewModelScope)
    }


    //val addToCartResponse = MutableLiveData<Event<Resource<Any>>>()
    val addToCartResponse = MutableLiveData<Long>()

    val addToCartResponseTwo = MutableLiveData<Long>()
 //   val updateCartResponse = MutableLiveData<Int>()
    val updateCartResponse = MutableLiveData<Event<Resource<Any>>>()

    fun addToCart(item: CartItem) = viewModelScope.launch {

//        Log.d("addtocart","inviewmodel")
//        addToCartResponse.value=Event(Resource.Loading)
//        repository.addToCart(item).onCompletion {
//            Log.d("addtocart","inviewmodel completed")
//            addToCartResponse.value=Event(Resource.Success(object :Any() { val data="Success"}))
//        }.launchIn(viewModelScope)

        addToCartResponse.value=repository.addToCartWithoutEmit(item)
    }



    val storeCartResponse = MutableLiveData<Event<Resource<Any>>>()
    fun storeCart(item: CartItem) = viewModelScope.launch {
        viewModelScope.launch {
            repository.storeCart(item).onEach {
                storeCartResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun addToCartWithoutEmit(item: CartItem,replace:Boolean=false) = viewModelScope.launch {

//        Log.d("addtocart","inviewmodel")
//        addToCartResponse.value=Event(Resource.Loading)
//        repository.addToCart(item).onCompletion {
//            Log.d("addtocart","inviewmodel completed")
//            addToCartResponse.value=Event(Resource.Success(object :Any() { val data="Success"}))
//        }.launchIn(viewModelScope)
//
//
        if (replace)
            addToCartResponseTwo.value= repository.addToCartReplace(item)
        else
            addToCartResponseTwo.value= repository.addToCartWithoutEmit(item)

    }

    fun update(item: CartItem) = viewModelScope.launch {
        Log.d("update","inviewmodel")
       // updateCartResponse.value= Event(Resource.Loading)

            Log.d("update","inviewmodel completed")
          //  updateCartResponse.value= repository.updateWithoutEmit(item)
    }

    fun updateCart(item:CartItem){
        viewModelScope.launch {
            repository.updateCart(item).onEach {
                updateCartResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }


    val removeFromCartResponse = MutableLiveData<Int>()

    fun remove(item: CartItem) = viewModelScope.launch {

            Log.d("removeFromCartResponse","inviewmodel completed")
            removeFromCartResponse.value= repository.deleteW(item)

    }

    val deleteItem = MutableLiveData<Event<Resource<Any>>>()

    fun deleteItem(item: CartItem) {
        viewModelScope.launch {
            repository.deleteCart(item).onEach {
                deleteItem.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    val checkItemResponse = MutableLiveData<CartItem>()

    fun checkItem(id: Int) = viewModelScope.launch {
        checkItemResponse.value= repository.getItem(id)
    }

    val getProductResponse = MutableLiveData<Event<Resource<Product>>>()

    fun getProduct(productId:Int) {
        viewModelScope.launch {
            repository.getProduct(productId).onEach {
                getProductResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    val addCommentResponse = MutableLiveData<Event<Resource<Comment>>>()

    fun addComment(productId:Int,name:String,comment:String) {
        viewModelScope.launch {
            repository.addComment(productId,name,comment).onEach {
                addCommentResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    val favResponse = MutableLiveData<Event<Resource<Any>>>()

    fun addFav(productId:Int) {
        viewModelScope.launch {
            repository.addFav(productId).onEach {
                favResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

    fun removeFav(productId:Int) {
        viewModelScope.launch {
            repository.removeFav(productId).onEach {
                favResponse.value = Event(it)
            }.launchIn(viewModelScope)
        }
    }

}