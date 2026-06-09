package com.inb.spendly.presentation.screens

import com.inb.spendly.presentation.FilterType

interface SharedCommand {
    data class UpdateDateRange(val range: FilterType): SharedCommand
}