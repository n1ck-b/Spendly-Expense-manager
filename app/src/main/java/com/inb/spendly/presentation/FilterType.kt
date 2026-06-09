package com.inb.spendly.presentation

import androidx.annotation.StringRes
import com.inb.spendly.R

enum class FilterType(@StringRes val titleResourceId: Int) {
    TODAY(R.string.today),
    THIS_WEEK(R.string.week),
    THIS_MONTH(R.string.month),
    THIS_YEAR(R.string.year)
}

fun allDateFiltersIds(): List<Int> =
    FilterType.entries.map { it.titleResourceId }