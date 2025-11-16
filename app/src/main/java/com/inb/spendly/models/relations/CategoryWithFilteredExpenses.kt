package com.inb.spendly.models.relations

data class CategoryWithFilteredExpenses(
    val categoryId: Long,
    val categoryName: String,
    val categoryColor: Long,
    val categoryIconId: Int,
    val expenseAmount: Float?
)