package uz.usoft.a24seven.network

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.usoft.a24seven.network.models.CartItem

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    fun getAll(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cartItem: CartItem):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReplace(cartItem: CartItem):Long

    @Delete
    suspend fun delete(cartItem: CartItem) : Int

    @Update
    suspend fun updateCartItem(cartItem: CartItem) : Int


    @Query("SELECT * FROM cart where id= :id")
    suspend fun getItem(id:Int): CartItem

    @Query("DELETE FROM cart")
    suspend fun emptyTheCart()
}