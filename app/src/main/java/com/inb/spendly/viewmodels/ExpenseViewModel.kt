package com.inb.spendly.viewmodels

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inb.spendly.api.RetrofitInstance
import com.inb.spendly.models.Category
import com.inb.spendly.models.Currencies
import com.inb.spendly.models.ExchangeRates
import com.inb.spendly.models.Expense
import com.inb.spendly.repository.CategoryDao
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
import com.inb.spendly.viewmodels.state.ExpenseState
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
import retrofit2.Response
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseViewModel(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
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

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    private val _state = MutableStateFlow(ExpenseState())
    val state = _state.asStateFlow()

    var selectedCurrency = mutableStateOf(Currencies.BYN)
        private set

    private var selectedFromListExpenseId = mutableLongStateOf(0)

    val categoriesList = categoryDao.getAllCategories()

    fun updateExpenseAmount(newAmount: Float) {
        _state.update { it.copy(
            amount = newAmount
        ) }
    }

    fun updateExpenseDate(newTimestamp: Long?) {

        val newDate =
            if (newTimestamp == null) null
            else Date(newTimestamp)

        _state.update { it.copy(
            date = newDate
        ) }
    }

    fun updateExpenseNote(newNote: String?) {
        _state.update { it.copy(
            note = newNote
        ) }
    }

    fun updateExpenseCategoryName(newName: String) {
        _state.update { it.copy(
            categoryName = newName
        ) }
    }

    fun updateSelectedCurrency(newCurrency: String) {
        selectedCurrency.value = Currencies.valueOf(newCurrency)
    }

    fun saveExpense() {
        if(_state.value.categoryName == null || _state.value.date == null) {
            viewModelScope.launch {
                _events.emit(UiEvent.ShowToastNotAllFieldsFilled)
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val response: Response<ExchangeRates>
            var exchangeRate: Map<String, Double>? = null

            if(selectedCurrency.value != Currencies.BYN) {
                try {
                    response = RetrofitInstance
                        .api.getExchangeRate(selectedCurrency.value.name)
                    if (!response.isSuccessful) {
                        _events.emit(UiEvent.ShowToastErrorGettingExchangeRates)
                        return@launch
                    }
                    exchangeRate = response.body()?.conversionRates
                } catch (e: Exception) {
                    _events.emit(UiEvent.ShowToastErrorGettingExchangeRates)
                    return@launch
                }
            }

            val rateToBYN =
                if(exchangeRate != null) exchangeRate["BYN"]
                else 1.0

            val category: Category? = categoryDao.getCategoryByName(_state.value.categoryName!!)

            val expense = Expense(
                id = _state.value.id,
                amount = _state.value.amount * rateToBYN!!.toFloat(),
                date = _state.value.date!!,
                note = _state.value.note,
                categoryId = category!!.id
            )
            expenseDao.upsertExpense(expense)
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
            val expenseWithCategory = expenseDao
                .getExpenseWithCategoryById(selectedFromListExpenseId.longValue).stateIn(viewModelScope)
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
            expenseDao.deleteExpense(selectedFromListExpenseId.longValue)
        }
    }
}