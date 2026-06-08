package com.inb.spendly.presentation.screens.categories

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
import com.inb.spendly.domain.entities.Category
import com.inb.spendly.domain.useCases.categories.AddCategoryUseCase
import com.inb.spendly.domain.useCases.categories.DeleteCategoryUseCase
import com.inb.spendly.domain.useCases.categories.ExistsCategoryByNameUseCase
import com.inb.spendly.domain.useCases.categories.GetCategoryByIdUseCase
import com.inb.spendly.domain.useCases.categories.GetExpensesByCategoriesUseCase
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
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val getExpensesByCategoriesUseCase: GetExpensesByCategoriesUseCase,
    private val existsCategoryByNameUseCase: ExistsCategoryByNameUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    sharedViewModel: SharedViewModel
) : ViewModel() {

    private val selectedDateFilter = sharedViewModel.selectedFilterType

    val categoriesList = selectedDateFilter
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
        }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(), emptyList())

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    private val _state = MutableStateFlow(CategoryState())
    val state = _state.asStateFlow()

    private var selectedFromListCategoryId = mutableLongStateOf(0)

    fun updateCategoryName(newName: String) {
        _state.update {
            it.copy(
                name = newName
            )
        }
    }

    fun updateCategoryColor(newColor: Color) {
        _state.update {
            it.copy(
                color = newColor
            )
        }
    }

    fun updateCategoryIconId(newIconId: String) {
        _state.update {
            it.copy(
                iconId = newIconId
            )
        }
    }

    fun saveCategory() {
        if (_state.value.name.isBlank()) {
            viewModelScope.launch {
                _events.emit(UiEvent.ShowToastNotAllFieldsFilled)
            }
            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            val categoryExists = existsCategoryByNameUseCase(_state.value.name)

            if (categoryExists && _state.value.id == 0L) {
                _events.emit(UiEvent.ShowToastCategoryAlreadyExists)
            } else {
                val newCategory = Category(
                    id = _state.value.id,
                    name = _state.value.name,
                    color = _state.value.color.toArgb(),
                    iconId = _state.value.iconId
                )
                addCategoryUseCase(newCategory)
                resetValues()
                _events.emit(UiEvent.CloseDialog)
            }
        }
    }

    fun updateState() {
        viewModelScope.launch(Dispatchers.IO) {
            val category = getCategoryByIdUseCase(
                selectedFromListCategoryId.longValue)
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
            deleteCategoryUseCase(selectedFromListCategoryId.longValue)
        }
    }

    fun updateSelectedCategoryId(newId: Long) {
        selectedFromListCategoryId.longValue = newId
    }

    fun resetValues() {
        _state.value = CategoryState()
    }
}