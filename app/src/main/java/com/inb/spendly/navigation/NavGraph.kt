package com.inb.spendly.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.inb.spendly.repository.ExpenseDatabase
import com.inb.spendly.screens.CategoriesScreen
import com.inb.spendly.screens.ExpenseHistoryScreen
import com.inb.spendly.screens.StatisticsScreen
import com.inb.spendly.viewmodels.CategoryViewModel
import com.inb.spendly.viewmodels.CategoryViewModelFactory
import com.inb.spendly.viewmodels.ExpenseViewModel
import com.inb.spendly.viewmodels.ExpenseViewModelFactory
import com.inb.spendly.viewmodels.SharedViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    expenseDatabase: ExpenseDatabase,
    sharedViewModel: SharedViewModel,
    expenseViewModel: ExpenseViewModel,
    categoryViewModel: CategoryViewModel
){
    NavHost(
        navController = navController,
        startDestination = ExpenseHistoryScreenRoute
    ) {
        composable<ExpenseHistoryScreenRoute> {

//            val expenseViewModel = viewModel<ExpenseViewModel>(
//                factory = ExpenseViewModelFactory(expenseDatabase.expenseDao, sharedViewModel)
//            )

            ExpenseHistoryScreen(paddingValues, expenseViewModel, sharedViewModel)
        }
        composable<CategoriesScreenRoute> {
            CategoriesScreen(paddingValues, categoryViewModel, sharedViewModel)
        }
        composable<StatisticsScreenRoute> {
//            val categoryViewModel = viewModel<CategoryViewModel>(
//                factory = CategoryViewModelFactory(expenseDatabase.categoryDao, sharedViewModel)
//            )

            StatisticsScreen(paddingValues, categoryViewModel, sharedViewModel)
        }
    }
}