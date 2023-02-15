package com.pap.majika.api

import com.pap.majika.models.Menu
import com.pap.majika.models.Response
import retrofit2.Call
import retrofit2.http.GET

interface MajikaApi {
    @GET("menu")
    fun getMenus(): Call<Response<List<Menu>>>
}