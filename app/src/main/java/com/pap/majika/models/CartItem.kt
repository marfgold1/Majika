package com.pap.majika.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cart_item", primaryKeys = ["name", "description"])
class CartItem  (
    name: String,
    description: String,
    quantity: Int,
        ) {
    @SerializedName("name")
    var name: String = name
    @SerializedName("description")
    var description: String = description
    @SerializedName("quantity")
    var quantity: Int = quantity
}