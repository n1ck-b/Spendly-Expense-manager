package com.inb.spendly.presentation.screens.expenses

import com.inb.spendly.data.models.Currencies

sealed interface ExpenseCommand {

    data class DeleteExpense(
        val expenseId: Long
    ): ExpenseCommand

    data object ReturnToList: ExpenseCommand

    data object AddExpense: ExpenseCommand

    data object SaveEditedExpense: ExpenseCommand

    data class InputAmount(val amount: Float): ExpenseCommand

    data class InputDate(val date: Long?): ExpenseCommand

    data class InputNote(val note: String?): ExpenseCommand

    data class InputCategoryName(val categoryName: String): ExpenseCommand

    data class InputSelectedCurrency(
        val selectedCurrency: String
    ): ExpenseCommand

    data class SelectAction(val expenseId: Long): ExpenseCommand

    data object EditExpense: ExpenseCommand

    data object SaveExpense: ExpenseCommand

}