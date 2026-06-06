package com.inb.spendly.data.repository

import androidx.compose.ui.graphics.toArgb
import com.inb.spendly.R
import com.inb.spendly.data.toDbModel
import com.inb.spendly.data.toCategoryEntities
import com.inb.spendly.data.toEntity
import com.inb.spendly.domain.entities.Category
import com.inb.spendly.domain.CategoryRepository
import com.inb.spendly.domain.entities.CategoryWithFilteredExpenses
import com.inb.spendly.domain.Constants.Companion.NO_CATEGORY
import com.inb.spendly.domain.ExpenseRepository
import com.inb.spendly.presentation.ui.theme.DefaultIconColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val expenseRepository: ExpenseRepository
): CategoryRepository {
    override suspend fun addCategory(category: Category) {
        categoryDao.upsertCategory(category.toDbModel())
    }

    override suspend fun deleteCategory(categoryId: Long) {
        val expensesForCategory = expenseRepository
            .getExpensesForCategory(categoryId)

        var withoutCategory = getCategoryByName(NO_CATEGORY)

        if (expensesForCategory.isNotEmpty()) {
            if (withoutCategory == null) {
                withoutCategory = Category(
                    name = NO_CATEGORY,
                    color = DefaultIconColor.toArgb(),
                    iconId = R.drawable.outline_image_24
                )
                addCategory(withoutCategory)
            }
            expensesForCategory.forEach { expense ->
                expense.categoryId = withoutCategory.id
            }
            expensesForCategory.forEach {
                expenseRepository.updateExpense(it)
            }
        }
        categoryDao.deleteCategoryById(categoryId)
    }

    override fun getAllExpensesByCategories(
        startDate: Long,
        endDate: Long
    ): Flow<List<CategoryWithFilteredExpenses>> {
        return categoryDao.getAllCategoriesWithExpensesForSelectedRange(startDate, endDate)
    }

    override suspend fun getCategoryById(categoryId: Long): Category {
        return categoryDao.getCategoryById(categoryId).toEntity()
    }

    override suspend fun getCategoryByName(categoryName: String): Category? {
        return categoryDao.getCategoryByName(categoryName)?.toEntity()
    }

    override suspend fun existsCategoryByName(categoryName: String): Boolean {
        return categoryDao.getCategoryByName(categoryName) != null
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.upsertCategory(category.toDbModel())
    }

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { it.toCategoryEntities() }
    }
}