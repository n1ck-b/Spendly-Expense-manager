package com.inb.spendly.domain

import com.inb.spendly.BuildConfig

class Constants {

    companion object {
        const val BASE_URL = "https://v6.exchangerate-api.com/v6/${BuildConfig.API_KEY}/"
    }

}