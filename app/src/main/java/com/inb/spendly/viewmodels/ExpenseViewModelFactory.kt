package com.inb.spendly.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inb.spendly.repository.ExpenseDao

class ExpenseViewModelFactory(
    private val expenseDao: ExpenseDao,
    private val sharedViewModel: SharedViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            return ExpenseViewModel(expenseDao, sharedViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}