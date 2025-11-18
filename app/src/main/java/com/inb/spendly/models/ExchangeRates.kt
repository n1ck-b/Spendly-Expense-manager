package com.inb.spendly.models

import com.google.gson.annotations.SerializedName

data class ExchangeRates (
    @SerializedName("base_code")
    val baseCode: String,
    @SerializedName("conversion_rates")
    val conversionRates: Map<String, Double>
)

data class ConversionRate (
    val USD: Float,
    val BYN: Float,
    val EUR: Float,
    val CNY: Float,
    val GBP: Float,
    val PLN: Float
)