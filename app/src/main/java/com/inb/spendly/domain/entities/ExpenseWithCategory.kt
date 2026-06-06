package com.inb.spendly.domain.entities

data class ExpenseWithCategory(
    val expense: Expense,
    val category: Category
)