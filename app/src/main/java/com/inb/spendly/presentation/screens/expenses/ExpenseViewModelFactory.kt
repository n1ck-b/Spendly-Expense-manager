package com.inb.spendly.presentation.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inb.spendly.data.repository.CategoryDao
import com.inb.spendly.data.repository.ExpenseDao
import com.inb.spendly.presentation.screens.SharedViewModel

class ExpenseViewModelFactory(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
    private val sharedViewModel: SharedViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            return ExpenseViewModel(expenseDao, categoryDao, sharedViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}