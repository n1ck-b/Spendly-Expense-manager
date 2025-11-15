package com.inb.spendly.api

import com.inb.spendly.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ExchangeRatesApi by lazy {
        retrofit.create(ExchangeRatesApi::class.java)
    }

}