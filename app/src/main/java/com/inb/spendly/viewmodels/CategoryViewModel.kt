package com.inb.spendly.viewmodels

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toColorLong
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inb.spendly.models.Category
import com.inb.spendly.repository.CategoryDao
import com.inb.spendly.util.FilterType
import com.inb.spendly.util.getTimestampForEndOfThisMonth
import com.inb.spendly.util.getTimestampForEndOfThisWeek
import com.inb.spendly.util.getTimestampForEndOfThisYear
import com.inb.spendly.util.getTimestampForEndOfToday
import com.inb.spendly.util.getTimestampForStartOfThisMonth
import com.inb.spendly.util.getTimestampForStartOfThisWeek
import com.inb.spendly.util.getTimestampForStartOfThisYear
import com.inb.spendly.util.getTimestampForStartOfToday
import com.inb.spendly.viewmodels.state.CategoryState
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

@OptIn(ExperimentalCoroutinesApi::class)
class CategoryViewModel(
    private val categoryDao: CategoryDao,
    private val sharedViewModel: SharedViewModel
): ViewModel() {

    private val selectedDateFilter = sharedViewModel.selectedFilterType

    val categoriesList = selectedDateFilter
        .flatMapLatest { filterType ->
            when(filterType) {
                FilterType.TODAY -> {
                    val startDate = getTimestampForStartOfToday()
                    val endDate = getTimestampForEndOfToday()
                    categoryDao.getAllCategoriesWithExpensesForSelectedRange(startDate, endDate)
                }
                FilterType.THIS_WEEK -> {
                    val startDate = getTimestampForStartOfThisWeek()
                    val endDate = getTimestampForEndOfThisWeek()
                    categoryDao.getAllCategoriesWithExpensesForSelectedRange(startDate, endDate)
                }
                FilterType.THIS_MONTH -> {
                    val startDate = getTimestampForStartOfThisMonth()
                    val endDate = getTimestampForEndOfThisMonth()
                    categoryDao.getAllCategoriesWithExpensesForSelectedRange(startDate, endDate)
                }
                FilterType.THIS_YEAR -> {
                    val startDate = getTimestampForStartOfThisYear()
                    val endDate = getTimestampForEndOfThisYear()
                    categoryDao.getAllCategoriesWithExpensesForSelectedRange(startDate, endDate)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    private val _state = MutableStateFlow(CategoryState())
    val state = _state.asStateFlow()

    var selectedFromListCategoryId = mutableLongStateOf(0)
        private set

    fun updateCategoryName(newName: String) {
        _state.update { it.copy(
            name = newName
        ) }
    }

    fun updateCategoryColor(newColor: Color) {
        _state.update { it.copy(
            color = newColor
        ) }
    }

    fun updateCategoryIconId(newIconId: Int) {
        _state.update { it.copy(
            iconId = newIconId
        ) }
    }

    fun saveCategory() {
        if(_state.value.name.isEmpty() || _state.value.name.isBlank()) {
            viewModelScope.launch {
                _events.emit(UiEvent.ShowToastNotAllFieldsFilled)
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            val existingCategory = categoryDao.getCategoryByName(_state.value.name)

            if(existingCategory != null && _state.value.id == 0L) {
                _events.emit(UiEvent.ShowToastCategoryAlreadyExists)
            }
            else {
                val newCategory = Category(
                    id = _state.value.id,
                    name = _state.value.name,
                    color = _state.value.color.toArgb(),
                    iconId = _state.value.iconId
                )
                categoryDao.upsertCategory(newCategory)
                resetValues()
                _events.emit(UiEvent.CloseDialog)
            }
        }
    }

    fun updateState() {
        viewModelScope.launch(Dispatchers.IO) {
            val category = categoryDao.getCategoryById(selectedFromListCategoryId.longValue)
            _state.value = CategoryState(
                id = category.id,
                name = category.name,
                color = Color(category.color),
                iconId = category.iconId
            )
        }
    }

    fun deleteCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            categoryDao.deleteCategoryById(selectedFromListCategoryId.longValue)
        }
    }

    fun updateSelectedCategoryId(newId: Long) {
        selectedFromListCategoryId.longValue = newId
    }

    fun resetValues() {
        _state.value = CategoryState()
    }
}