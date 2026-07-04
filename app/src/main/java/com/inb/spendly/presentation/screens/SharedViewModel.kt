package com.inb.spendly.presentation.screens

import androidx.lifecycle.ViewModel
import com.inb.spendly.presentation.FilterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedViewModel @Inject constructor(): ViewModel() {

    private val _selectedFilterType = MutableStateFlow(FilterType.THIS_MONTH)
    val selectedFilterType = _selectedFilterType.asStateFlow()

    fun processCommand(command: SharedCommand) {
        when(command) {
            is SharedCommand.UpdateDateRange -> {
                _selectedFilterType.value = command.range
            }
        }
    }

}