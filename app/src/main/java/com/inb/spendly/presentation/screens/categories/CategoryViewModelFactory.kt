package com.inb.spendly.presentation.screens.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inb.spendly.data.repository.CategoryDao
import com.inb.spendly.data.repository.ExpenseDao
import com.inb.spendly.presentation.screens.SharedViewModel

class CategoryViewModelFactory(
    private val categoryDao: CategoryDao,
    private val expenseDao: ExpenseDao,
    private val sharedViewModel: SharedViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(categoryDao, expenseDao, sharedViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}