package com.inb.spendly.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.inb.spendly.models.Category
import com.inb.spendly.models.Expense

@Database(
    entities = [
        Expense::class,
        Category::class
    ],
    version = 7,
    exportSchema = false,
)
abstract class ExpenseDatabase() : RoomDatabase() {

    abstract val expenseDao: ExpenseDao
    abstract val categoryDao: CategoryDao

    companion object {

        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getInstance(context: Context): ExpenseDatabase {
            synchronized(this) {
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }

    }
}
