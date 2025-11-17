package com.inb.spendly.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.inb.spendly.repository.ExpenseConverter
import java.util.Date

@Entity(tableName = "expenses")
@TypeConverters(ExpenseConverter::class)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var amount: Float,
    var date: Date,
    var categoryId: Long,
    var note: String?,
)
