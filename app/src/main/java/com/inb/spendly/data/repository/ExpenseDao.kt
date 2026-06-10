package com.inb.spendly.data.repository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.inb.spendly.data.models.ExpenseDbModel
import com.inb.spendly.data.models.relations.ExpenseWithCategoryDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Upsert
    suspend fun upsertExpense(expenseDbModel: ExpenseDbModel)

    @Query(
        "SELECT *" +
        "FROM expenses " +
        "WHERE expenses.date BETWEEN :startDate AND :endDate " +
        "ORDER BY expenses.date DESC"
    )
    fun getAllExpensesWithCategoriesForSelectedRange(startDate: Long, endDate: Long):
            Flow<List<ExpenseWithCategoryDbModel>>

    @Query(
        "SELECT *" +
        "FROM expenses " +
        "WHERE expenses.id = :expenseId " +
        "ORDER BY expenses.date DESC"
    )
    fun getExpenseWithCategoryById(expenseId: Long): ExpenseWithCategoryDbModel

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpense(expenseId: Long)

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId")
    suspend fun getExpensesForCategory(categoryId: Long): List<ExpenseDbModel>
}