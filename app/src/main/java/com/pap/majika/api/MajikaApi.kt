package com.pap.majika.api

import com.pap.majika.models.Menu
import com.pap.majika.models.PaymentStatus
import com.pap.majika.models.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MajikaApi {
    @GET("menu")
    suspend fun getMenus(): Response<List<Menu>>

    @POST("payment/{transaction_id}")
    suspend fun postPayment(@Path("transaction_id") transaction_id: String): PaymentStatus

    companion object {
        private val ins = RetrofitClient.getInstance().create(MajikaApi::class.java)
        fun getInstance(): MajikaApi { return ins }
    }
}