package com.inb.spendly.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.inb.spendly.repository.CategoryDao

class CategoryViewModelFactory(
    private val categoryDao: CategoryDao,
    private val sharedViewModel: SharedViewModel
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(categoryDao, sharedViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}