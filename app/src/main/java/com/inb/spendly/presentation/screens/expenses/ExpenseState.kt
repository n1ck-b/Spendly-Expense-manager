package com.inb.spendly.presentation.screens.expenses

import com.inb.spendly.domain.entities.ExpenseWithCategory

sealed interface ExpenseState {

    data class Loaded(
        val expenses: List<ExpenseWithCategory>,
        val dialogState: ExpenseDialogState
    ) : ExpenseState

    data object Loading: ExpenseState
}
