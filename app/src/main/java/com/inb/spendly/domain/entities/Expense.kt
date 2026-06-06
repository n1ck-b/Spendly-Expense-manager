package com.inb.spendly.domain.entities

import java.util.Date

data class Expense(
    var id: Long = 0,
    var amount: Float,
    var date: Date,
    var categoryId: Long,
    var note: String?,
)