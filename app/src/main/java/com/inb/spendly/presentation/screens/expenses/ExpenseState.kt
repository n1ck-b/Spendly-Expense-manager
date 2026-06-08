package com.inb.spendly.presentation.screens.expenses

import com.inb.spendly.data.models.Currencies
import com.inb.spendly.domain.entities.ExpenseWithCategory
import java.util.Date

sealed interface ExpenseState {

    data class Loaded(
        val expenses: List<ExpenseWithCategory>,
        val dialogState: ExpenseDialogState
    ) : ExpenseState

    data object Loading: ExpenseState
}
