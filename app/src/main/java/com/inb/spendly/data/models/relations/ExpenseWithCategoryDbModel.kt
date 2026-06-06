package com.inb.spendly.data.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.inb.spendly.data.models.CategoryDbModel
import com.inb.spendly.data.models.ExpenseDbModel

data class ExpenseWithCategoryDbModel(
    @Embedded val expenseDbModel: ExpenseDbModel,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val categoryDbModel: CategoryDbModel
)