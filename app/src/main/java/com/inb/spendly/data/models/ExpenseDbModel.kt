package com.inb.spendly.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.inb.spendly.data.repository.ExpenseConverter
import java.util.Date

@Entity(tableName = "expenses")
@TypeConverters(ExpenseConverter::class)
data class ExpenseDbModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var amount: Float,
    var date: Long,
    var categoryId: Long,
    var note: String?,
)
