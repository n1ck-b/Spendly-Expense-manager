package com.inb.spendly.domain.useCases.expenses

import com.inb.spendly.domain.ExpenseRepository
import com.inb.spendly.domain.entities.Expense
import com.inb.spendly.domain.entities.ExpenseWithCategory
import kotlinx.coroutines.flow.Flow

class GetExpenseWithCategoryUseCase(
    private val expenseRepository: ExpenseRepository
) {

    suspend operator fun invoke(expenseId: Long): Flow<ExpenseWithCategory> {
        return expenseRepository.getExpenseWithCategory(expenseId)
    }

}