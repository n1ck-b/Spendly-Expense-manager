package com.inb.spendly.presentation.screens.expenses

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inb.spendly.data.models.Currencies
import com.inb.spendly.domain.Utils.getTimestampForEndOfThisMonth
import com.inb.spendly.domain.Utils.getTimestampForEndOfThisWeek
import com.inb.spendly.domain.Utils.getTimestampForEndOfThisYear
import com.inb.spendly.domain.Utils.getTimestampForEndOfToday
import com.inb.spendly.domain.Utils.getTimestampForStartOfThisMonth
import com.inb.spendly.domain.Utils.getTimestampForStartOfThisWeek
import com.inb.spendly.domain.Utils.getTimestampForStartOfThisYear
import com.inb.spendly.domain.Utils.getTimestampForStartOfToday
import com.inb.spendly.domain.useCases.categories.GetAllCategoriesUseCase
import com.inb.spendly.domain.useCases.expenses.AddExpenseUseCase
import com.inb.spendly.domain.useCases.expenses.DeleteExpenseUseCase
import com.inb.spendly.domain.useCases.expenses.GetExpenseWithCategoryUseCase
import com.inb.spendly.domain.useCases.expenses.GetExpensesWithCategoriesUseCase
import com.inb.spendly.presentation.FilterType
import com.inb.spendly.presentation.screens.SharedViewModel
import com.inb.spendly.presentation.screens.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val getExpensesWithCategoriesUseCase: GetExpensesWithCategoriesUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val getExpenseWithCategoryUseCase: GetExpenseWithCategoryUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    getAllCategoriesUseCase: GetAllCategoriesUseCase,
    sharedViewModel: SharedViewModel
) : ViewModel() {

    private val selectedDateFilter = sharedViewModel.selectedFilterType

    val expensesList = selectedDateFilter
        .flatMapLatest { filterType ->
            when (filterType) {
                FilterType.TODAY -> {
                    val startDate = getTimestampForStartOfToday()
                    val endDate = getTimestampForEndOfToday()
                    getExpensesWithCategoriesUseCase(startDate, endDate)
                }

                FilterType.THIS_WEEK -> {
                    val startDate = getTimestampForStartOfThisWeek()
                    val endDate = getTimestampForEndOfThisWeek()
                    getExpensesWithCategoriesUseCase(startDate, endDate)
                }

                FilterType.THIS_MONTH -> {
                    val startDate = getTimestampForStartOfThisMonth()
                    val endDate = getTimestampForEndOfThisMonth()
                    getExpensesWithCategoriesUseCase(startDate, endDate)
                }

                FilterType.THIS_YEAR -> {
                    val startDate = getTimestampForStartOfThisYear()
                    val endDate = getTimestampForEndOfThisYear()
                    getExpensesWithCategoriesUseCase(startDate, endDate)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    private val _state = MutableStateFlow(ExpenseState())
    val state = _state.asStateFlow()

    var selectedCurrency = mutableStateOf(Currencies.BYN)
        private set

    private var selectedFromListExpenseId = mutableLongStateOf(0)

    val categoriesList = getAllCategoriesUseCase()

    fun updateExpenseAmount(newAmount: Float) {
        _state.update {
            it.copy(
                amount = newAmount
            )
        }
    }

    fun updateExpenseDate(newTimestamp: Long?) {

        val newDate =
            if (newTimestamp == null) null
            else Date(newTimestamp)

        _state.update {
            it.copy(
                date = newDate
            )
        }
    }

    fun updateExpenseNote(newNote: String?) {
        _state.update {
            it.copy(
                note = newNote
            )
        }
    }

    fun updateExpenseCategoryName(newName: String) {
        _state.update {
            it.copy(
                categoryName = newName
            )
        }
    }

    fun updateSelectedCurrency(newCurrency: String) {
        selectedCurrency.value = Currencies.valueOf(newCurrency)
    }

    fun saveExpense() {
        if (_state.value.categoryName == null || _state.value.date == null) {
            viewModelScope.launch {
                _events.emit(UiEvent.ShowToastNotAllFieldsFilled)
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            val added = addExpenseUseCase(
                categoryName = _state.value.categoryName!!,
                expenseId = _state.value.id,
                expenseAmount = _state.value.amount,
                expenseDate = _state.value.date!!,
                expenseNote = _state.value.note,
                selectedCurrency = selectedCurrency.value
            )

            if (!added) {
                _events.emit(UiEvent.ShowToastErrorGettingExchangeRates)
                return@launch
            }

            resetValues()
            _events.emit(UiEvent.CloseDialog)
        }
    }

    fun resetValues() {
        _state.value = ExpenseState()
        selectedCurrency.value = Currencies.BYN
    }

    fun updateSelectedExpenseId(newId: Long) {
        selectedFromListExpenseId.longValue = newId
    }

    fun updateState() {
        viewModelScope.launch(Dispatchers.IO) {
            val expenseWithCategory = getExpenseWithCategoryUseCase(
                selectedFromListExpenseId.longValue
            ).stateIn(viewModelScope)
            _state.value = ExpenseState(
                id = expenseWithCategory.value.expense.id,
                amount = expenseWithCategory.value.expense.amount,
                date = expenseWithCategory.value.expense.date,
                note = expenseWithCategory.value.expense.note,
                categoryName = expenseWithCategory.value.category.name
            )
        }
    }

    fun deleteExpense() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteExpenseUseCase(selectedFromListExpenseId.longValue)
        }
    }
}