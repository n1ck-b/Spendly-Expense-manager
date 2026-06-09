package com.inb.spendly.presentation.screens.categories

import com.inb.spendly.domain.entities.CategoryWithFilteredExpenses

sealed interface CategoryState {

    data class Loaded(
        val categories: List<CategoryWithFilteredExpenses>,
        val dialogState: CategoryDialogState
    ) : CategoryState

    data object Loading: CategoryState

}