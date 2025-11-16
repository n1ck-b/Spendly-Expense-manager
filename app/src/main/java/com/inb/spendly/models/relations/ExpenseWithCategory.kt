package com.inb.spendly.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.inb.spendly.models.Category
import com.inb.spendly.models.Expense

data class ExpenseWithCategory(
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: Category
)