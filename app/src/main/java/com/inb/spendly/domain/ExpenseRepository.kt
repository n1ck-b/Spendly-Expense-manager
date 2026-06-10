package com.inb.spendly.domain

import com.inb.spendly.domain.entities.Expense
import com.inb.spendly.domain.entities.ExpenseWithCategory
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun getExpensesForCategory(categoryId: Long): List<Expense>

    suspend fun updateExpense(expense: Expense)

    fun getExpensesWithCategories(startDate: Long, endDate: Long): Flow<List<ExpenseWithCategory>>

    suspend fun addExpense(expense: Expense)

    suspend fun getExpenseWithCategory(expenseId: Long): ExpenseWithCategory

    suspend fun deleteExpense(expenseId: Long)
}