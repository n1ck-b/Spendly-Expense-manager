package com.inb.spendly.repository

import androidx.room.Dao
import androidx.room.Query
import com.inb.spendly.models.relations.CategoryWithFilteredExpenses
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query(
        "SELECT categories.id AS categoryId," +
        " categories.name AS categoryName," +
        " categories.color AS categoryColor," +
        " categories.iconId AS categoryIconId, " +
        " SUM(expenses.amount) AS expenseAmount " +
        "FROM categories LEFT JOIN expenses " +
        "ON categories.id = expenses.categoryId " +
        "AND expenses.date BETWEEN :startDate AND :endDate " +
        "GROUP BY categories.id " +
        "ORDER BY categories.name"
    )
    fun getAllCategoriesWithExpensesForSelectedRange(startDate: Long, endDate: Long): Flow<List<CategoryWithFilteredExpenses>>
}