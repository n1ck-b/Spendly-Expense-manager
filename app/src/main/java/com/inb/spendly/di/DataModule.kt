package com.inb.spendly.di

import android.content.Context
import com.inb.spendly.data.api.ExchangeRatesApi
import com.inb.spendly.data.api.RetrofitInstance
import com.inb.spendly.data.repository.CategoryDao
import com.inb.spendly.data.repository.CategoryRepositoryImpl
import com.inb.spendly.data.repository.ExpenseDao
import com.inb.spendly.data.repository.ExpenseDatabase
import com.inb.spendly.data.repository.ExpenseRepositoryImpl
import com.inb.spendly.domain.CategoryRepository
import com.inb.spendly.domain.ExpenseRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    fun bindExpenseRepository(
        impl: ExpenseRepositoryImpl
    ): ExpenseRepository

    companion object {

        @Provides
        fun provideExpenseDatabase(
            @ApplicationContext context: Context
        ): ExpenseDatabase {
            return ExpenseDatabase.getInstance(context)
        }

        @Provides
        @Singleton
        fun provideExpenseDao(
            expenseDatabase: ExpenseDatabase
        ): ExpenseDao {
            return expenseDatabase.expenseDao
        }

        @Provides
        @Singleton
        fun provideCategoryDao(
            expenseDatabase: ExpenseDatabase
        ): CategoryDao {
            return expenseDatabase.categoryDao
        }

        @Provides
        @Singleton
        fun provideRetrofitInstance(): RetrofitInstance = RetrofitInstance

        @Provides
        @Singleton
        fun provideExchangeRatesApi(
            retrofitInstance: RetrofitInstance
        ): ExchangeRatesApi {
            return retrofitInstance.api
        }

    }

}