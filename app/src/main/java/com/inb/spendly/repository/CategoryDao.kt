package com.inb.spendly.repository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.inb.spendly.models.Category
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

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE name = :name")
    suspend fun getCategoryByName(name: String): Category?

    @Upsert
    suspend fun upsertCategory(category: Category)

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Long): Category

    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Long)
}