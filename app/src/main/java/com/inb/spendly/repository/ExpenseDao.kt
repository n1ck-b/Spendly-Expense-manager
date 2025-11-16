package com.inb.spendly.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.inb.spendly.models.Expense
import com.inb.spendly.models.relations.ExpenseWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query(
        "SELECT *" +
        "FROM expenses " +
        "WHERE expenses.date BETWEEN :startDate AND :endDate " +
        "ORDER BY expenses.date DESC"
    )
    fun getAllExpensesWithCategoriesForSelectedRange(startDate: Long, endDate: Long): Flow<List<ExpenseWithCategory>>
}