package com.inb.spendly.data.repository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.inb.spendly.data.models.CategoryDbModel
import com.inb.spendly.domain.entities.CategoryWithFilteredExpenses
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
    fun getAllCategoriesWithExpensesForSelectedRange(startDate: Long, endDate: Long):
            Flow<List<CategoryWithFilteredExpenses>>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryDbModel>>

    @Query("SELECT * FROM categories WHERE name = :name")
    suspend fun getCategoryByName(name: String): CategoryDbModel?

    @Upsert
    suspend fun upsertCategory(category: CategoryDbModel)

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Long): CategoryDbModel

    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Long)
}