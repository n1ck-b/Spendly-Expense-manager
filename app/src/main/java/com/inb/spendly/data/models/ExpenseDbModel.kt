package com.inb.spendly.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseDbModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var amount: Float,
    var date: Long,
    var categoryId: Long,
    var note: String?,
)
