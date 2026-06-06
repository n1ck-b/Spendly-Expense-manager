package com.inb.spendly.domain.useCases.expenses

import com.inb.spendly.domain.ExpenseRepository
import com.inb.spendly.domain.entities.ExpenseWithCategory
import kotlinx.coroutines.flow.Flow

class GetExpensesWithCategoriesUseCase(
    private val expenseRepository: ExpenseRepository
) {

    operator fun invoke(startDate: Long, endDate: Long): Flow<List<ExpenseWithCategory>> {
        return expenseRepository.getExpensesWithCategories(startDate, endDate)
    }

}