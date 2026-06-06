package com.inb.spendly.domain

import com.inb.spendly.data.models.ExpenseDbModel
import com.inb.spendly.domain.entities.Category
import com.inb.spendly.domain.entities.CategoryWithFilteredExpenses
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun addCategory(category: Category)

    suspend fun deleteCategory(categoryId: Long)

    fun getAllExpensesByCategories(startDate: Long, endDate: Long):
            Flow<List<CategoryWithFilteredExpenses>>

    suspend fun getCategoryById(categoryId: Long): Category

    suspend fun getCategoryByName(categoryName: String): Category?

    suspend fun existsCategoryByName(categoryName: String): Boolean

    suspend fun updateCategory(category: Category)

    fun getAllCategories(): Flow<List<Category>>

}