package com.inb.spendly.domain.useCases.expenses

import com.inb.spendly.domain.ExpenseRepository
import com.inb.spendly.domain.entities.ExpenseWithCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpenseWithCategoryUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository
) {

    suspend operator fun invoke(expenseId: Long): ExpenseWithCategory {
        return expenseRepository.getExpenseWithCategory(expenseId)
    }

}