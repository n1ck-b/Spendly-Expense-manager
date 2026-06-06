package com.inb.spendly.data.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.inb.spendly.data.models.Category
import com.inb.spendly.data.models.Expense

data class ExpenseWithCategory(
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category
)