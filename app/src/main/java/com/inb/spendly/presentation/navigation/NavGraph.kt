package com.inb.spendly.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.inb.spendly.presentation.components.BottomNavBarItem
import com.inb.spendly.presentation.screens.SharedViewModel
import com.inb.spendly.presentation.screens.categories.CategoriesScreen
import com.inb.spendly.presentation.screens.expenses.ExpenseHistoryScreen
import com.inb.spendly.presentation.screens.statistics.StatisticsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    bottomNavBarItems: List<BottomNavBarItem>
){
    NavHost(
        navController = navController,
        startDestination = ExpenseHistoryScreenRoute
    ) {
        composable<ExpenseHistoryScreenRoute> {
            ExpenseHistoryScreen(
                sharedViewModel = sharedViewModel,
                navController = navController,
                bottomNavBarItems = bottomNavBarItems
            )
        }
        composable<CategoriesScreenRoute> {
            CategoriesScreen(
                sharedViewModel = sharedViewModel,
                navController = navController,
                bottomNavBarItems = bottomNavBarItems
            )
        }
        composable<StatisticsScreenRoute> {
            StatisticsScreen(
                sharedViewModel = sharedViewModel,
                navController = navController,
                bottomNavBarItems = bottomNavBarItems
            )
        }
    }
}