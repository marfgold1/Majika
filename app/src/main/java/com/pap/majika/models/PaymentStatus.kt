package com.pap.majika.models

import com.google.gson.annotations.SerializedName

data class PaymentStatus (
    val transactionId: String = "",
    @SerializedName("status") val status: String = "UNRECOGNIZED",
)