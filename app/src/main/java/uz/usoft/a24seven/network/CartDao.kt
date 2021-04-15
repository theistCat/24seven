package uz.usoft.a24seven.network

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.usoft.a24seven.network.models.CartItem

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    fun getAll(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cartItem: CartItem)

    @Delete
    suspend fun delete(cartItem: CartItem)

    @Update
    suspend fun updateCartItem(cartItem: CartItem)


    @Query("DELETE FROM cart")
    suspend fun emptyTheCart()
}