package com.inb.spendly.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.inb.spendly.screens.CategoriesScreen
import com.inb.spendly.screens.ExpenseHistoryScreen
import com.inb.spendly.screens.StatisticsScreen

@Composable
fun NavGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = ExpenseHistoryScreenRoute
    ) {
        composable<ExpenseHistoryScreenRoute> {
            ExpenseHistoryScreen(paddingValues)
        }
        composable<CategoriesScreenRoute> {
            CategoriesScreen(paddingValues)
        }
        composable<StatisticsScreenRoute> {
            StatisticsScreen(paddingValues)
        }
    }
}