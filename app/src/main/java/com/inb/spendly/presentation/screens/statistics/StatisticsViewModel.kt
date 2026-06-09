package com.inb.spendly.presentation.screens.statistics

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
import com.inb.spendly.domain.useCases.categories.GetExpensesByCategoriesUseCase
import com.inb.spendly.presentation.FilterType
import com.inb.spendly.presentation.screens.SharedViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getExpensesByCategoriesUseCase: GetExpensesByCategoriesUseCase,
    sharedViewModel: SharedViewModel
) : ViewModel() {

    private val selectedDateFilter = sharedViewModel.selectedFilterType

    private val _state =
        MutableStateFlow<StatisticsState>(StatisticsState.Loading)
    val state = _state.asStateFlow()

    init {
        selectedDateFilter
            .onEach {
                _state.value = StatisticsState.Loading
            }
            .flatMapLatest { filterType ->
                when (filterType) {
                    FilterType.TODAY -> {
                        val startDate = getTimestampForStartOfToday()
                        val endDate = getTimestampForEndOfToday()
                        getExpensesByCategoriesUseCase(startDate, endDate)
                    }

                    FilterType.THIS_WEEK -> {
                        val startDate = getTimestampForStartOfThisWeek()
                        val endDate = getTimestampForEndOfThisWeek()
                        getExpensesByCategoriesUseCase(startDate, endDate)
                    }

                    FilterType.THIS_MONTH -> {
                        val startDate = getTimestampForStartOfThisMonth()
                        val endDate = getTimestampForEndOfThisMonth()
                        getExpensesByCategoriesUseCase(startDate, endDate)
                    }

                    FilterType.THIS_YEAR -> {
                        val startDate = getTimestampForStartOfThisYear()
                        val endDate = getTimestampForEndOfThisYear()
                        getExpensesByCategoriesUseCase(startDate, endDate)
                    }
                }
            }.onEach { newList ->
                _state.update { prevState ->
                    if (prevState is StatisticsState.Loaded) {
                        prevState.copy(categoriesWithExpenses = newList)
                    } else {
                        StatisticsState.Loaded(newList)
                    }

                }
            }.launchIn(viewModelScope)
    }
}