package com.inb.spendly.domain.useCases.expenses

import com.inb.spendly.domain.ExpenseRepository
import javax.inject.Inject

class DeleteExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
) {

    suspend operator fun invoke(expenseId: Long) {
        expenseRepository.deleteExpense(expenseId)
    }
}