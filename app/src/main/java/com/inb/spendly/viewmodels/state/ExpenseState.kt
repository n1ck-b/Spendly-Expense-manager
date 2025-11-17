package com.inb.spendly.viewmodels.state
import java.util.Date

data class ExpenseState(
    val id: Long = 0,
    val amount: Float = 0f,
    val date: Date? = Date(),
    val note: String? = null,
    val categoryName: String? = null
)
