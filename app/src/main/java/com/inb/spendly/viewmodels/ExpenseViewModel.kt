package com.inb.spendly.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inb.spendly.repository.ExpenseDao
import com.inb.spendly.util.FilterType
import com.inb.spendly.util.getTimestampForEndOfThisMonth
import com.inb.spendly.util.getTimestampForEndOfThisWeek
import com.inb.spendly.util.getTimestampForEndOfThisYear
import com.inb.spendly.util.getTimestampForEndOfToday
import com.inb.spendly.util.getTimestampForStartOfThisMonth
import com.inb.spendly.util.getTimestampForStartOfThisWeek
import com.inb.spendly.util.getTimestampForStartOfThisYear
import com.inb.spendly.util.getTimestampForStartOfToday
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseViewModel(
    val expenseDao: ExpenseDao,
    private val sharedViewModel: SharedViewModel
): ViewModel() {

    private val selectedDateFilter = sharedViewModel.selectedFilterType

    val expensesList = selectedDateFilter
        .flatMapLatest { filterType ->
            when(filterType) {
                FilterType.TODAY -> {
                    val startDate = getTimestampForStartOfToday()
                    val endDate = getTimestampForEndOfToday()
                    expenseDao.getAllExpensesWithCategoriesForSelectedRange(startDate, endDate)
                }
                FilterType.THIS_WEEK -> {
                    val startDate = getTimestampForStartOfThisWeek()
                    val endDate = getTimestampForEndOfThisWeek()
                    expenseDao.getAllExpensesWithCategoriesForSelectedRange(startDate, endDate)
                }
                FilterType.THIS_MONTH -> {
                    val startDate = getTimestampForStartOfThisMonth()
                    val endDate = getTimestampForEndOfThisMonth()
                    expenseDao.getAllExpensesWithCategoriesForSelectedRange(startDate, endDate)
                }
                FilterType.THIS_YEAR -> {
                    val startDate = getTimestampForStartOfThisYear()
                    val endDate = getTimestampForEndOfThisYear()
                    expenseDao.getAllExpensesWithCategoriesForSelectedRange(startDate, endDate)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
}