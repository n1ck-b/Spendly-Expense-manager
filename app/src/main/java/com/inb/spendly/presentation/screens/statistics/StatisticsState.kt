package com.inb.spendly.presentation.screens.statistics

import com.inb.spendly.domain.entities.CategoryWithFilteredExpenses

sealed interface StatisticsState {

    data class Loaded(
        val categoriesWithExpenses: List<CategoryWithFilteredExpenses>,
    ) : StatisticsState

    data object Loading: StatisticsState
}
