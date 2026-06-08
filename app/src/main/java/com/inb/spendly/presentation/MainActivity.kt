package com.inb.spendly.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inb.spendly.R
import com.inb.spendly.presentation.components.BottomNavBarItem
import com.inb.spendly.presentation.components.BottomNavigationBar
import com.inb.spendly.presentation.components.FloatingActionButtonAdd
import com.inb.spendly.presentation.navigation.CategoriesScreenRoute
import com.inb.spendly.presentation.navigation.ExpenseHistoryScreenRoute
import com.inb.spendly.presentation.navigation.NavGraph
import com.inb.spendly.presentation.navigation.StatisticsScreenRoute
import com.inb.spendly.presentation.screens.SharedViewModel
import com.inb.spendly.presentation.ui.theme.SpendlyTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            SpendlyTheme {
                val navController = rememberNavController()
                val backStackEntry = navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry.value?.destination

                val bottomNavBarItems = listOf(
                    BottomNavBarItem(
                        route = CategoriesScreenRoute,
                        name = stringResource(R.string.categories_screen_name),
                        icon = R.drawable.outline_dashboard_24
                    ),
                    BottomNavBarItem(
                        route = ExpenseHistoryScreenRoute,
                        name = stringResource(R.string.expense_history_screen_name),
                        icon = R.drawable.outline_receipt_24
                    ),
                    BottomNavBarItem(
                        route = StatisticsScreenRoute,
                        name = stringResource(R.string.statistics_screen_name),
                        icon = R.drawable.outline_bar_chart_24
                    )
                )

                Scaffold(
                    floatingActionButton = {
                        when (currentDestination?.route?.let { Class.forName(it) }) {
                            CategoriesScreenRoute::class.java -> FloatingActionButtonAdd {
                                sharedViewModel.updateShowCategoryDialog(true)
                            }

                            StatisticsScreenRoute::class.java -> {}
                        }
                    },
                    bottomBar = {
                        BottomNavigationBar(navController, bottomNavBarItems)
                    }
                ) { padding ->
                    NavGraph(
                        navController = navController,
                        paddingValues = padding,
                        sharedViewModel = sharedViewModel,
                        bottomNavBarItems = bottomNavBarItems
                    )
                }
            }
        }
    }
}