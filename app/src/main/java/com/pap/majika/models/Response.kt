package com.pap.majika.models

import com.google.gson.annotations.SerializedName

data class Response<T> (
    @SerializedName("data")
    var data: T? = null,
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("size")
    var size: Int? = null,
)