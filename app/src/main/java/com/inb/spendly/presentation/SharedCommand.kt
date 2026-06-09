package com.inb.spendly.presentation

interface SharedCommand {
    data class UpdateDateRange(val range: String): SharedCommand
}