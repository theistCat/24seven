package a24seven.uz.network.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cart")
data class CartItem(
    @PrimaryKey
    val id: Int=-1,
    val count:Int
)