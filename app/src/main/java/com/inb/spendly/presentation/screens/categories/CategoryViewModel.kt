package com.inb.spendly.presentation.screens.categories

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
import com.inb.spendly.domain.useCases.categories.ExistsCategoryByNameAndIdUseCase
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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    private val existsCategoryByNameAndIdUseCase: ExistsCategoryByNameAndIdUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    sharedViewModel: SharedViewModel
) : ViewModel() {

    private val selectedDateFilter = sharedViewModel.selectedFilterType

    private val _state = MutableStateFlow<CategoryState>(
        CategoryState.Loading
    )
    val state = _state.asStateFlow()

    init {
        selectedDateFilter
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
                }.onEach { newList ->
                    _state.update { prevState ->
                        if (prevState is CategoryState.Loaded) {
                            prevState.copy(categories = newList)
                        } else {
                            CategoryState.Loaded(
                                categories = newList,
                                dialogState = CategoryDialogState.Closed
                            )
                        }
                    }
                }
            }.launchIn(viewModelScope)
    }

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    private fun saveCategory() {
        val currentState = _state.value

        if (currentState is CategoryState.Loaded
            && currentState.dialogState is CategoryDialogState.AddingCategory
        ) {
            if (currentState.dialogState.name.isBlank()) {
                viewModelScope.launch {
                    _events.emit(UiEvent.ShowToastNotAllFieldsFilled)
                }
                return
            }

            viewModelScope.launch(Dispatchers.IO) {

                val categoryExists = existsCategoryByNameUseCase(
                    currentState.dialogState.name
                )

                if (categoryExists) {
                    _events.emit(UiEvent.ShowToastCategoryAlreadyExists)
                } else {
                    val newCategory = Category(
                        id = currentState.dialogState.id,
                        name = currentState.dialogState.name,
                        color = currentState.dialogState.color.toArgb(),
                        iconId = currentState.dialogState.iconId
                    )
                    addCategoryUseCase(newCategory)
                }
            }
        }
    }

    private fun saveEditedCategory() {
        val currentState = _state.value

        if (currentState is CategoryState.Loaded
            && currentState.dialogState is CategoryDialogState.AddingCategory
        ) {
            viewModelScope.launch {
                val categoryExistsByName = existsCategoryByNameUseCase(
                    currentState.dialogState.name
                )

                if (categoryExistsByName) {
                    val categoryExistsByNameAndId = existsCategoryByNameAndIdUseCase(
                        categoryName = currentState.dialogState.name,
                        categoryId = currentState.dialogState.id
                    )
                    if (!categoryExistsByNameAndId) {
                        _events.emit(UiEvent.ShowToastCategoryAlreadyExists)
                        return@launch
                    }
                }
                val newCategory = Category(
                    id = currentState.dialogState.id,
                    name = currentState.dialogState.name,
                    color = currentState.dialogState.color.toArgb(),
                    iconId = currentState.dialogState.iconId
                )
                addCategoryUseCase(newCategory)
            }
        }
    }

    private fun deleteCategory() {
        val currentState = _state.value
        if (currentState is CategoryState.Loaded
            && currentState.dialogState is CategoryDialogState.SelectingAction
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                deleteCategoryUseCase(currentState.dialogState.categoryId)
            }
        }
    }

    private fun returnToList() {
        _state.update { prevState ->
            if (prevState is CategoryState.Loaded) {
                prevState.copy(dialogState = CategoryDialogState.Closed)
            } else {
                prevState
            }
        }
    }

    fun processCommand(command: CategoryCommand) {
        when (command) {

            CategoryCommand.AddCategory -> {
                val currentState = _state.value
                if (currentState is CategoryState.Loaded) {
                    _state.update { prevState ->
                        if (prevState is CategoryState.Loaded) {
                            prevState.copy(
                                dialogState = CategoryDialogState.AddingCategory()
                            )
                        } else {
                            prevState
                        }
                    }
                }
            }

            is CategoryCommand.DeleteCategory -> {
                deleteCategory()
                returnToList()
            }

            CategoryCommand.EditCategory -> {
                val currentState = _state.value
                if (currentState is CategoryState.Loaded
                    && currentState.dialogState is CategoryDialogState.SelectingAction
                ) {
                    viewModelScope.launch(Dispatchers.IO) {
                        val category = getCategoryByIdUseCase(
                            currentState.dialogState.categoryId
                        )
                        _state.update { prevState ->
                            if (prevState is CategoryState.Loaded) {
                                prevState.copy(
                                    dialogState = CategoryDialogState.AddingCategory(
                                        id = category.id,
                                        name = category.name,
                                        color = Color(category.color),
                                        iconId = category.iconId,
                                        editing = true
                                    )
                                )
                            } else {
                                prevState
                            }
                        }
                    }
                }
            }

            is CategoryCommand.InputColor -> {
                _state.update { prevState ->
                    if (prevState is CategoryState.Loaded
                        && prevState.dialogState is CategoryDialogState.AddingCategory
                    ) {
                        val newDialogState =
                            prevState.dialogState.copy(color = command.color)
                        prevState.copy(dialogState = newDialogState)
                    } else {
                        prevState
                    }
                }
            }

            is CategoryCommand.InputIconId -> {
                _state.update { prevState ->
                    if (prevState is CategoryState.Loaded
                        && prevState.dialogState is CategoryDialogState.AddingCategory
                    ) {
                        val newDialogState =
                            prevState.dialogState.copy(iconId = command.iconId)
                        prevState.copy(dialogState = newDialogState)
                    } else {
                        prevState
                    }
                }
            }

            is CategoryCommand.InputName -> {
                _state.update { prevState ->
                    if (prevState is CategoryState.Loaded
                        && prevState.dialogState is CategoryDialogState.AddingCategory
                    ) {
                        val newDialogState =
                            prevState.dialogState.copy(name = command.name)
                        prevState.copy(dialogState = newDialogState)
                    } else {
                        prevState
                    }
                }
            }

            CategoryCommand.ReturnToList -> {
                returnToList()
            }

            CategoryCommand.SaveCategory -> {
                saveCategory()
                returnToList()
            }

            CategoryCommand.SaveEditedCategory -> {
                saveEditedCategory()
                returnToList()
            }

            is CategoryCommand.SelectAction -> {
                _state.update { prevState ->
                    if (prevState is CategoryState.Loaded) {
                        prevState.copy(
                            dialogState = CategoryDialogState.SelectingAction(
                                command.categoryId
                            )
                        )
                    } else {
                        prevState
                    }
                }
            }
        }
    }
}