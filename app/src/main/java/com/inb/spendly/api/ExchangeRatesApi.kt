package com.inb.spendly.api

import com.inb.spendly.models.ExchangeRates
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRatesApi {

    @GET("latest/{baseCode}")
    suspend fun getExchangeRate(@Path("baseCode") baseCode: String): Response<ExchangeRates>
}