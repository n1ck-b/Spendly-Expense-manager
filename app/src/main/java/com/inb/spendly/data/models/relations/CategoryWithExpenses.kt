package com.inb.spendly.data.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.inb.spendly.data.models.Category
import com.inb.spendly.data.models.Expense

data class CategoryWithExpenses(
    @Embedded
    val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val expenses: List<Expense>
)