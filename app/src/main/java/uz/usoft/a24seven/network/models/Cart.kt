package uz.usoft.a24seven.network.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.Unit

@Entity(tableName = "Cart")
data class CartItem(
    @PrimaryKey
    val id: Int=-1,
    val count:Int
)