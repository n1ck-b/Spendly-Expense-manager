package com.inb.spendly.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    sharedViewModel: SharedViewModel
){
    NavHost(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        navController = navController,
        startDestination = ExpenseHistoryScreenRoute,
        enterTransition = { fadeIn(tween(200)) },
        exitTransition = { fadeOut(tween(200)) },
        popEnterTransition = { fadeIn(tween(200)) },
        popExitTransition = { fadeOut(tween(200)) }
    ) {
        composable<ExpenseHistoryScreenRoute> {
            ExpenseHistoryScreen(
                sharedViewModel = sharedViewModel,
                navController = navController
            )
        }
        composable<CategoriesScreenRoute> {
            CategoriesScreen(
                sharedViewModel = sharedViewModel,
                navController = navController
            )
        }
        composable<StatisticsScreenRoute> {
            StatisticsScreen(
                sharedViewModel = sharedViewModel,
                navController = navController
            )
        }
    }
}