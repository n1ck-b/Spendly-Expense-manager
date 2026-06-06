package com.inb.spendly.data.api

import com.inb.spendly.data.models.ExchangeRates
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRatesApi {

    @GET("latest/{baseCode}")
    suspend fun getExchangeRate(@Path("baseCode") baseCode: String): Response<ExchangeRates>
}