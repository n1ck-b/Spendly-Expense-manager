package com.inb.spendly.presentation.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inb.spendly.data.repository.CategoryDao
import com.inb.spendly.data.repository.ExpenseDao
import com.inb.spendly.domain.useCases.categories.GetAllCategoriesUseCase
import com.inb.spendly.domain.useCases.expenses.AddExpenseUseCase
import com.inb.spendly.domain.useCases.expenses.DeleteExpenseUseCase
import com.inb.spendly.domain.useCases.expenses.GetExpenseWithCategoryUseCase
import com.inb.spendly.domain.useCases.expenses.GetExpensesWithCategoriesUseCase
import com.inb.spendly.presentation.screens.SharedViewModel

class ExpenseViewModelFactory(
    private val getExpensesWithCategoriesUseCase: GetExpensesWithCategoriesUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val getExpenseWithCategoryUseCase: GetExpenseWithCategoryUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val sharedViewModel: SharedViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            return ExpenseViewModel(
                getExpensesWithCategoriesUseCase,
                addExpenseUseCase,
                getExpenseWithCategoryUseCase,
                deleteExpenseUseCase,
                getAllCategoriesUseCase,
                sharedViewModel
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}