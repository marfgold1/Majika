package com.pap.majika.api

class RetrofitClient {
    companion object {
        private val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/v1/")
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()

        fun getInstance(): retrofit2.Retrofit {
            return retrofit
        }
    }
}