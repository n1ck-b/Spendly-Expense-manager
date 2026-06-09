package com.inb.spendly.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inb.spendly.domain.Utils.getTimestampForEndOfThisMonth
import com.inb.spendly.domain.Utils.getTimestampForEndOfThisWeek
import com.inb.spendly.domain.Utils.getTimestampForEndOfThisYear
import com.inb.spendly.domain.Utils.getTimestampForEndOfToday
import com.inb.spendly.domain.Utils.getTimestampForStartOfThisMonth
import com.inb.spendly.domain.Utils.getTimestampForStartOfThisWeek
import com.inb.spendly.domain.Utils.getTimestampForStartOfThisYear
import com.inb.spendly.domain.Utils.getTimestampForStartOfToday
import com.inb.spendly.domain.entities.CategoryWithFilteredExpenses
import com.inb.spendly.domain.useCases.categories.GetExpensesByCategoriesUseCase
import com.inb.spendly.presentation.FilterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedViewModel @Inject constructor(
    private val getExpensesByCategoriesUseCase: GetExpensesByCategoriesUseCase
): ViewModel() {

    private val _selectedDateRange = MutableStateFlow("Today")
    val selectedDateRange = _selectedDateRange.asStateFlow()

    var categoriesWithExpenses = emptyFlow<List<CategoryWithFilteredExpenses>>()

    val selectedFilterType: StateFlow<FilterType> =
        selectedDateRange
            .map { range ->
                when (range) {
                    FilterType.TODAY.string -> FilterType.TODAY
                    FilterType.TODAY.stringRu -> FilterType.TODAY
                    FilterType.THIS_WEEK.stringRu -> FilterType.THIS_WEEK
                    FilterType.THIS_WEEK.string -> FilterType.THIS_WEEK
                    FilterType.THIS_MONTH.stringRu -> FilterType.THIS_MONTH
                    FilterType.THIS_MONTH.string -> FilterType.THIS_MONTH
                    FilterType.THIS_YEAR.stringRu -> FilterType.THIS_YEAR
                    FilterType.THIS_YEAR.string -> FilterType.THIS_YEAR
                    else -> FilterType.TODAY
                }
            }.onEach {range ->
                when(range) {
                    FilterType.TODAY -> {
                        val startDate = getTimestampForStartOfToday()
                        val endDate = getTimestampForEndOfToday()
                        categoriesWithExpenses = getExpensesByCategoriesUseCase(startDate, endDate)
                    }

                    FilterType.THIS_WEEK -> {
                        val startDate = getTimestampForStartOfThisWeek()
                        val endDate = getTimestampForEndOfThisWeek()
                        categoriesWithExpenses = getExpensesByCategoriesUseCase(startDate, endDate)
                    }

                    FilterType.THIS_MONTH -> {
                        val startDate = getTimestampForStartOfThisMonth()
                        val endDate = getTimestampForEndOfThisMonth()
                        categoriesWithExpenses = getExpensesByCategoriesUseCase(startDate, endDate)
                    }

                    FilterType.THIS_YEAR -> {
                        val startDate = getTimestampForStartOfThisYear()
                        val endDate = getTimestampForEndOfThisYear()
                        categoriesWithExpenses = getExpensesByCategoriesUseCase(startDate, endDate)
                    }
                }

            }
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                FilterType.TODAY
            )

    fun updateDateRange(newRange: String) {
        _selectedDateRange.value = newRange
    }

}