package com.inb.spendly.domain.entities

data class Category(
    var id: Long = 0,
    var name: String,
    var color: Int,
    var iconId: Int,
)