package com.inb.spendly.data

import com.inb.spendly.data.models.CategoryDbModel
import com.inb.spendly.data.models.ExpenseDbModel
import com.inb.spendly.data.models.relations.ExpenseWithCategoryDbModel
import com.inb.spendly.domain.entities.Category
import com.inb.spendly.domain.entities.Expense
import com.inb.spendly.domain.entities.ExpenseWithCategory
import java.util.Date

fun Category.toDbModel(): CategoryDbModel {
    return CategoryDbModel(id = id, name = name, color = color, iconId = iconId)
}

fun CategoryDbModel.toEntity(): Category {
    return Category(id, name, color, iconId)
}

fun Expense.toDbModel(): ExpenseDbModel {
    return ExpenseDbModel(id = id, amount = amount, date = date.time, categoryId = categoryId, note = note)
}

fun ExpenseDbModel.toEntity(): Expense {
    return Expense(id, amount, Date(date), categoryId, note)
}

fun ExpenseWithCategoryDbModel.toEntity(): ExpenseWithCategory {
    return ExpenseWithCategory(expenseDbModel.toEntity(), categoryDbModel.toEntity())
}

fun ExpenseWithCategory.toDbModel(): ExpenseWithCategoryDbModel {
    return ExpenseWithCategoryDbModel(expense.toDbModel(),
        category.toDbModel())
}

fun List<ExpenseDbModel>.toExpenseEntities(): List<Expense> {
    return this.map { it.toEntity() }
}

fun List<CategoryDbModel>.toCategoryEntities(): List<Category> {
    return this.map { it.toEntity() }
}

fun List<ExpenseWithCategoryDbModel>.toExpenseWithCategoryEntities(): List<ExpenseWithCategory> {
    return this.map { it.toEntity() }
}