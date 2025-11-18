package com.inb.spendly.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.inb.spendly.repository.ExpenseDatabase
import com.inb.spendly.screens.CategoriesScreen
import com.inb.spendly.screens.ExpenseHistoryScreen
import com.inb.spendly.screens.StatisticsScreen
import com.inb.spendly.viewmodels.CategoryViewModel
import com.inb.spendly.viewmodels.ExpenseViewModel
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
            ExpenseHistoryScreen(paddingValues, expenseViewModel, sharedViewModel)
        }
        composable<CategoriesScreenRoute> {
            CategoriesScreen(paddingValues, categoryViewModel, sharedViewModel)
        }
        composable<StatisticsScreenRoute> {
            StatisticsScreen(paddingValues, categoryViewModel, sharedViewModel)
        }
    }
}