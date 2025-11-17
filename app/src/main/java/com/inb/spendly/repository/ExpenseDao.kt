package com.inb.spendly.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.inb.spendly.models.Expense
import com.inb.spendly.models.relations.ExpenseWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Upsert
    suspend fun upsertExpense(expense: Expense)

    @Query(
        "SELECT *" +
        "FROM expenses " +
        "WHERE expenses.date BETWEEN :startDate AND :endDate " +
        "ORDER BY expenses.date DESC"
    )
    fun getAllExpensesWithCategoriesForSelectedRange(startDate: Long, endDate: Long): Flow<List<ExpenseWithCategory>>

    @Query(
        "SELECT *" +
        "FROM expenses " +
        "WHERE expenses.id = :expenseId " +
        "ORDER BY expenses.date DESC"
    )
    fun getExpenseWithCategoryById(expenseId: Long): Flow<ExpenseWithCategory>

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpense(expenseId: Long)
}