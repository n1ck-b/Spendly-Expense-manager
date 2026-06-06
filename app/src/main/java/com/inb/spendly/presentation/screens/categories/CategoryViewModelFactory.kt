package com.inb.spendly.presentation.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inb.spendly.data.repository.CategoryDao
import com.inb.spendly.data.repository.ExpenseDao
import com.inb.spendly.domain.useCases.categories.AddCategoryUseCase
import com.inb.spendly.domain.useCases.categories.DeleteCategoryUseCase
import com.inb.spendly.domain.useCases.categories.ExistsCategoryByNameUseCase
import com.inb.spendly.domain.useCases.categories.GetCategoryByIdUseCase
import com.inb.spendly.domain.useCases.categories.GetExpensesByCategoriesUseCase
import com.inb.spendly.presentation.screens.SharedViewModel

class CategoryViewModelFactory(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val getExpensesByCategoriesUseCase: GetExpensesByCategoriesUseCase,
    private val existsCategoryByNameUseCase: ExistsCategoryByNameUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val sharedViewModel: SharedViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(
                addCategoryUseCase,
                deleteCategoryUseCase,
                getExpensesByCategoriesUseCase,
                existsCategoryByNameUseCase,
                getCategoryByIdUseCase,
                sharedViewModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}