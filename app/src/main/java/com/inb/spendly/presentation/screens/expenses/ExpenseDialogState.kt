package com.inb.spendly.presentation.screens.expenses

import com.inb.spendly.data.models.Currencies
import com.inb.spendly.domain.entities.Category
import kotlinx.coroutines.flow.Flow
import java.util.Date

sealed interface ExpenseDialogState {

    data object Closed: ExpenseDialogState

    data class AddingExpense(
        val id: Long = 0,
        val amount: Float = 0f,
        val date: Date? = Date(),
        val note: String? = null,
        val category: Category,
        val selectedCurrency: Currencies = Currencies.BYN,
        val categories: List<Category>
    ): ExpenseDialogState

    data class SelectingAction(val expenseId: Long): ExpenseDialogState

}