package com.inb.spendly.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inb.spendly.presentation.FilterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedViewModel @Inject constructor(): ViewModel() {

    private val _selectedDateRange = MutableStateFlow("Today")
    val selectedDateRange = _selectedDateRange.asStateFlow()

    var showExpenseDialog = mutableStateOf(false)
        private set

    var showCategoryDialog = mutableStateOf(false)
        private set

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
            }
            .stateIn(
                viewModelScope,
                SharingStarted.Companion.Eagerly,
                FilterType.TODAY
            )

    fun updateDateRange(newRange: String) {
        _selectedDateRange.value = newRange
    }

    fun updateShowExpenseDialog(newValue: Boolean) {
        showExpenseDialog.value = newValue
    }

    fun updateShowCategoryDialog(newValue: Boolean) {
        showCategoryDialog.value = newValue
    }

}