package com.inb.spendly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inb.spendly.navigation.CategoriesScreenRoute
import com.inb.spendly.navigation.ExpenseHistoryScreenRoute
import com.inb.spendly.navigation.NavGraph
import com.inb.spendly.navigation.StatisticsScreenRoute
import com.inb.spendly.ui.components.BottomNavBarItem
import com.inb.spendly.ui.components.BottomNavigationBar
import com.inb.spendly.ui.components.FloatingActionButtonAdd
import com.inb.spendly.ui.theme.SpendlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        GlobalScope.launch {
//            val response = RetrofitInstance.api.getExchangeRate("BYN")
//            Log.d("ExchangeRates", "Response: ${response.body()?.conversionRates}")
//        }
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
                        when(currentDestination?.route?.let { Class.forName(it) }) {
                            ExpenseHistoryScreenRoute::class.java -> FloatingActionButtonAdd {

                            }
                            CategoriesScreenRoute::class.java -> FloatingActionButtonAdd {

                            }
                            StatisticsScreenRoute::class.java -> {}
                        }
                    },
                    bottomBar = {
                        BottomNavigationBar(navController, bottomNavBarItems)
                    }
                ) { padding ->
                    NavGraph(navController, padding)
                }
            }
        }
    }
}

