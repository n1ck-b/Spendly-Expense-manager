package com.inb.spendly.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.inb.spendly.data.models.CategoryDbModel
import com.inb.spendly.data.models.ExpenseDbModel

@Database(
    entities = [
        ExpenseDbModel::class,
        CategoryDbModel::class
    ],
    version = 8,
    exportSchema = false,
)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract val expenseDao: ExpenseDao
    abstract val categoryDao: CategoryDao

    companion object {

        @Volatile
        private var instance: ExpenseDatabase? = null

        fun getInstance(context: Context): ExpenseDatabase {

            instance?.let { return it }

            synchronized(this) {
                instance?.let { return it }

                return Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_db"
                ).build().also {
                    instance = it
                }
            }
        }

    }
}
