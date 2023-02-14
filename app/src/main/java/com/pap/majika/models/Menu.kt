package com.pap.majika.models

import com.google.gson.annotations.SerializedName

class Menu (
    name: String,
    description: String,
    currency: String,
    price: Int,
    sold: Int,
    type: String,
        ) {
    @SerializedName("name")
    var name: String = name
    @SerializedName("description")
    var description: String = description
    @SerializedName("currency")
    var currency: String = currency
    @SerializedName("price")
    var price: Int = price
    @SerializedName("sold")
    var sold: Int = sold
    @SerializedName("type")
    var type: String = type
}