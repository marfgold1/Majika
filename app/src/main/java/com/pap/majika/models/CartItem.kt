package com.pap.majika.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cart_item")
class CartItem  (
    name: String,
    quantity: Int,
        ) {
    @PrimaryKey
    @SerializedName("name")
    var name: String = name
    @SerializedName("quantity")
    var quantity: Int = quantity
}