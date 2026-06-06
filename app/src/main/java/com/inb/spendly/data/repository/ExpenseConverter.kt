package com.inb.spendly.data.repository

import androidx.room.TypeConverter
import java.util.Date

class ExpenseConverter {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let {
            Date(it)
        }
    }

}