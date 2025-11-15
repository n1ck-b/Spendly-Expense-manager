package com.inb.spendly.repository

import androidx.room.Dao
import androidx.room.Insert
import com.inb.spendly.models.Expense

@Dao
interface ExpenseDao {
    @Insert
    fun insertExpense(expense: Expense)
}