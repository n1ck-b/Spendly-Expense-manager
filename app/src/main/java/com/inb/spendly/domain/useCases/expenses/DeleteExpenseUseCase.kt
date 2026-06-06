package com.inb.spendly.domain.useCases.expenses

import com.inb.spendly.data.api.ExchangeRatesApi
import com.inb.spendly.data.models.Currencies
import com.inb.spendly.data.models.ExchangeRates
import com.inb.spendly.domain.CategoryRepository
import com.inb.spendly.domain.ExpenseRepository
import com.inb.spendly.domain.entities.Expense
import retrofit2.Response
import java.util.Date

class DeleteExpenseUseCase(
    private val expenseRepository: ExpenseRepository,
) {

    suspend operator fun invoke(expenseId: Long) {
        expenseRepository.deleteExpense(expenseId)
    }
}