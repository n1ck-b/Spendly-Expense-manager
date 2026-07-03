package com.inb.spendly.presentation.screens.expenses

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
import com.inb.spendly.domain.useCases.categories.GetAllCategoriesUseCase
import com.inb.spendly.domain.useCases.expenses.AddExpenseUseCase
import com.inb.spendly.domain.useCases.expenses.DeleteExpenseUseCase
import com.inb.spendly.domain.useCases.expenses.GetExpenseWithCategoryUseCase
import com.inb.spendly.domain.useCases.expenses.GetExpensesWithCategoriesUseCase
import com.inb.spendly.presentation.FilterType
import com.inb.spendly.presentation.screens.SharedViewModel
import com.inb.spendly.presentation.screens.UiEvent
import com.inb.spendly.presentation.screens.expenses.ExpenseDialogState.AddingExpense
import com.inb.spendly.presentation.screens.expenses.ExpenseDialogState.Closed
import com.inb.spendly.presentation.screens.expenses.ExpenseDialogState.SelectingAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    sharedViewModel: SharedViewModel
) : ViewModel() {

    private val selectedDateFilter = sharedViewModel.selectedFilterType

    private val _state =
        MutableStateFlow<ExpenseState>(ExpenseState.Loading)
    val state = _state.asStateFlow()

    init {
        selectedDateFilter
            .onEach {
                _state.value = ExpenseState.Loading
            }
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
            }.onEach { newList ->
                _state.update { prevState ->
                    if (prevState is ExpenseState.Loaded) {
                        prevState.copy(expenses = newList)
                    } else {
                        ExpenseState.Loaded(newList, Closed)
                    }

                }
            }.launchIn(viewModelScope)
    }

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    private fun saveExpense() {

        val currentState = _state.value

        if (currentState is ExpenseState.Loaded
            && currentState.dialogState is AddingExpense
        ) {
            if (currentState.dialogState.date == null) {
                viewModelScope.launch {
                    _events.emit(UiEvent.ShowToastNotAllFieldsFilled)
                }
                return
            }

            viewModelScope.launch(Dispatchers.IO) {

                val added = addExpenseUseCase(
                    categoryName = currentState.dialogState.category.name,
                    expenseId = currentState.dialogState.id,
                    expenseAmount = currentState.dialogState.amount,
                    expenseDate = currentState.dialogState.date,
                    expenseNote = currentState.dialogState.note,
                    selectedCurrency = currentState.dialogState.selectedCurrency
                )

                if (!added) {
                    _events.emit(UiEvent.ShowToastErrorGettingExchangeRates)
                    return@launch
                }
            }
        }
    }

    private fun deleteExpense() {
        val currentState = _state.value
        if (currentState is ExpenseState.Loaded
            && currentState.dialogState is SelectingAction
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                deleteExpenseUseCase(currentState.dialogState.expenseId)
            }
        }
    }

    private fun returnToList() {
        _state.update { prevState ->
            if (prevState is ExpenseState.Loaded) {
                prevState.copy(dialogState = Closed)
            } else {
                prevState
            }
        }
    }

    fun processCommand(command: ExpenseCommand) {

        when (command) {
            ExpenseCommand.AddExpense -> {

                viewModelScope.launch {

                    val categories = getAllCategoriesUseCase()

                    _state.update { prevState ->
                        if (prevState is ExpenseState.Loaded) {
                            prevState.copy(
                                dialogState = AddingExpense(
                                    categories = categories,
                                    category = categories.first()
                                )
                            )
                        } else {
                            prevState
                        }
                    }
                }
            }

            is ExpenseCommand.DeleteExpense -> {
                deleteExpense()
                returnToList()
            }

            is ExpenseCommand.InputAmount -> {
                _state.update { prevState ->
                    if (prevState is ExpenseState.Loaded
                        && prevState.dialogState is AddingExpense
                    ) {
                        val newDialogState =
                            prevState.dialogState.copy(amount = command.amount)
                        prevState.copy(dialogState = newDialogState)
                    } else {
                        prevState
                    }
                }
            }

            is ExpenseCommand.InputCategory -> {
                _state.update { prevState ->
                    if (prevState is ExpenseState.Loaded
                        && prevState.dialogState is AddingExpense
                    ) {
                        val newDialogState =
                            prevState.dialogState.copy(category = command.category)
                        prevState.copy(dialogState = newDialogState)
                    } else {
                        prevState
                    }
                }
            }

            is ExpenseCommand.InputDate -> {

                val newDate =
                    if (command.date == null) null
                    else Date(command.date)

                _state.update { prevState ->
                    if (prevState is ExpenseState.Loaded
                        && prevState.dialogState is AddingExpense
                    ) {
                        val newDialogState =
                            prevState.dialogState.copy(date = newDate)
                        prevState.copy(dialogState = newDialogState)
                    } else {
                        prevState
                    }
                }
            }

            is ExpenseCommand.InputNote -> {
                _state.update { prevState ->
                    if (prevState is ExpenseState.Loaded
                        && prevState.dialogState is AddingExpense
                    ) {
                        val newDialogState =
                            prevState.dialogState.copy(note = command.note)
                        prevState.copy(dialogState = newDialogState)
                    } else {
                        prevState
                    }
                }
            }

            is ExpenseCommand.InputSelectedCurrency -> {
                _state.update { prevState ->
                    if (prevState is ExpenseState.Loaded
                        && prevState.dialogState is AddingExpense
                    ) {
                        val newDialogState = prevState.dialogState.copy(
                            selectedCurrency = command.selectedCurrency
                        )
                        prevState.copy(dialogState = newDialogState)
                    } else {
                        prevState
                    }
                }
            }

            ExpenseCommand.ReturnToList -> {
                returnToList()
            }

            ExpenseCommand.SaveEditedExpense -> {
                saveExpense()
                returnToList()
            }

            ExpenseCommand.EditExpense -> {
                viewModelScope.launch(Dispatchers.IO) {

                    val currentState = _state.value

                    if (currentState is ExpenseState.Loaded
                        && currentState.dialogState is SelectingAction
                    ) {
                        val expenseWithCategory = getExpenseWithCategoryUseCase(
                            currentState.dialogState.expenseId
                        )

                        val categories = getAllCategoriesUseCase()

                        _state.update { prevState ->
                            if (prevState is ExpenseState.Loaded) {
                                prevState.copy(
                                    dialogState = AddingExpense(
                                        id = expenseWithCategory.expense.id,
                                        amount = expenseWithCategory.expense.amount,
                                        date = expenseWithCategory.expense.date,
                                        note = expenseWithCategory.expense.note,
                                        category = expenseWithCategory.category,
                                        categories = categories
                                    )
                                )
                            } else {
                                prevState
                            }
                        }
                    }
                }
            }

            is ExpenseCommand.SelectAction -> {
                _state.update { prevState ->
                    if (prevState is ExpenseState.Loaded) {
                        prevState.copy(
                            dialogState = SelectingAction(command.expenseId)
                        )
                    } else {
                        prevState
                    }
                }
            }

            ExpenseCommand.SaveExpense -> {
                saveExpense()
                returnToList()
            }
        }
    }
}