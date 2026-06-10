package com.inb.spendly.data.repository

import com.inb.spendly.data.toDbModel
import com.inb.spendly.data.toEntity
import com.inb.spendly.data.toExpenseEntities
import com.inb.spendly.data.toExpenseWithCategoryEntities
import com.inb.spendly.domain.ExpenseRepository
import com.inb.spendly.domain.entities.Expense
import com.inb.spendly.domain.entities.ExpenseWithCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {
    override suspend fun getExpensesForCategory(categoryId: Long): List<Expense> {
        return expenseDao.getExpensesForCategory(categoryId).toExpenseEntities()
    }

    override suspend fun updateExpense(expense: Expense) {
        expenseDao.upsertExpense(expense.toDbModel())
    }

    override fun getExpensesWithCategories(
        startDate: Long,
        endDate: Long
    ): Flow<List<ExpenseWithCategory>> {
        return expenseDao.getAllExpensesWithCategoriesForSelectedRange(startDate, endDate)
            .map { it.toExpenseWithCategoryEntities() }
    }

    override suspend fun addExpense(expense: Expense) {
        expenseDao.upsertExpense(expense.toDbModel())
    }

    override suspend fun getExpenseWithCategory(expenseId: Long): ExpenseWithCategory {
        return expenseDao.getExpenseWithCategoryById(expenseId).toEntity()
    }

    override suspend fun deleteExpense(expenseId: Long) {
        expenseDao.deleteExpense(expenseId)
    }
}