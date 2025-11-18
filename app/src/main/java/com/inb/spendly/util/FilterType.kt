package com.inb.spendly.util

enum class FilterType(val string: String, val stringRu: String) {
    TODAY("Today", "Сегодня"),
    THIS_WEEK("This week", "Текущая неделя"),
    THIS_MONTH("This month", "Текущий месяц"),
    THIS_YEAR("This year", "Текущий год")
}