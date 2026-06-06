package com.inb.spendly.data.models

import com.google.gson.annotations.SerializedName

data class ExchangeRates (
    @SerializedName("base_code")
    val baseCode: String,
    @SerializedName("conversion_rates")
    val conversionRates: Map<String, Double>
)