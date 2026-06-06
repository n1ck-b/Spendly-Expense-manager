package com.inb.spendly.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.inb.spendly.presentation.screens.SharedViewModel
import com.inb.spendly.presentation.screens.categories.CategoriesScreen
import com.inb.spendly.presentation.screens.expenses.ExpenseHistoryScreen
import com.inb.spendly.presentation.screens.statistics.StatisticsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    sharedViewModel: SharedViewModel
){
    NavHost(
        navController = navController,
        startDestination = ExpenseHistoryScreenRoute
    ) {
        composable<ExpenseHistoryScreenRoute> {
            ExpenseHistoryScreen(paddingValues, sharedViewModel = sharedViewModel)
        }
        composable<CategoriesScreenRoute> {
            CategoriesScreen(paddingValues, sharedViewModel = sharedViewModel)
        }
        composable<StatisticsScreenRoute> {
            StatisticsScreen(paddingValues, sharedViewModel = sharedViewModel)
        }
    }
}