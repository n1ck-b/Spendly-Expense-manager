package com.inb.spendly.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.inb.spendly.data.repository.ExpenseDatabase
import com.inb.spendly.presentation.screens.categories.CategoriesScreen
import com.inb.spendly.presentation.screens.expenses.ExpenseHistoryScreen
import com.inb.spendly.presentation.screens.statistics.StatisticsScreen
import com.inb.spendly.presentation.screens.categories.CategoryViewModel
import com.inb.spendly.presentation.screens.expenses.ExpenseViewModel
import com.inb.spendly.presentation.screens.SharedViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
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