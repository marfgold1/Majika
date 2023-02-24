package com.pap.majika.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cart_item", primaryKeys = ["name"])
class CartItem  (
    name: String,
    quantity: Int,
        ) {
    @SerializedName("name")
    var name: String = name
    @SerializedName("quantity")
    var quantity: Int = quantity
}