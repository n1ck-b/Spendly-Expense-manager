package com.inb.spendly.domain.useCases.expenses

import com.inb.spendly.data.api.ExchangeRatesApi
import com.inb.spendly.data.models.Currencies
import com.inb.spendly.data.models.ExchangeRates
import com.inb.spendly.domain.CategoryRepository
import com.inb.spendly.domain.ExpenseRepository
import com.inb.spendly.domain.entities.Expense
import retrofit2.Response
import java.util.Date
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
    private val api: ExchangeRatesApi
) {

    suspend operator fun invoke(
        categoryName: String,
        expenseId: Long,
        expenseAmount: Float,
        expenseNote: String?,
        expenseDate: Date,
        selectedCurrency: Currencies
    ): Boolean {

        val response: Response<ExchangeRates>
        var exchangeRate: Map<String, Double>? = null

        if(selectedCurrency != Currencies.BYN) {
            try {
                response = api.getExchangeRate(selectedCurrency.name)
                if (!response.isSuccessful) {
                    return false
                }
                exchangeRate = response.body()?.conversionRates
            } catch (_: Exception) {
                return false
            }
        }

        val rateToBYN =
            if(exchangeRate != null) exchangeRate["BYN"]
            else 1.0

        val category = categoryRepository.getCategoryByName(categoryName)

        val expense = Expense(
            id = expenseId,
            amount = expenseAmount * rateToBYN!!.toFloat(),
            date = expenseDate,
            note = expenseNote,
            categoryId = category!!.id
        )
        expenseRepository.addExpense(expense)
        return true
    }
}