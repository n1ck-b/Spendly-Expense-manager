package com.inb.spendly.presentation

import androidx.annotation.StringRes
import com.inb.spendly.R

enum class FilterType(@StringRes val titleResourceId: Int) {
    TODAY(R.string.filter_today),
    THIS_WEEK(R.string.filter_week),
    THIS_MONTH(R.string.filter_month),
    THIS_YEAR(R.string.filter_year)
}