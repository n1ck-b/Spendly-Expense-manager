package com.inb.spendly

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inb.spendly.api.RetrofitInstance
import com.inb.spendly.navigation.CategoriesScreenRoute
import com.inb.spendly.navigation.ExpenseHistoryScreenRoute
import com.inb.spendly.navigation.NavGraph
import com.inb.spendly.navigation.StatisticsScreenRoute
import com.inb.spendly.repository.ExpenseDatabase
import com.inb.spendly.screens.AddingCategoryDialog
import com.inb.spendly.screens.AddingExpenseDialog
import com.inb.spendly.ui.components.BottomNavBarItem
import com.inb.spendly.ui.components.BottomNavigationBar
import com.inb.spendly.ui.components.FloatingActionButtonAdd
import com.inb.spendly.ui.theme.SpendlyTheme
import com.inb.spendly.util.FilterType
import com.inb.spendly.viewmodels.CategoryViewModel
import com.inb.spendly.viewmodels.CategoryViewModelFactory
import com.inb.spendly.viewmodels.ExpenseViewModel
import com.inb.spendly.viewmodels.ExpenseViewModelFactory
import com.inb.spendly.viewmodels.SharedViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        try {
//            GlobalScope.launch {
//                val response = RetrofitInstance.api.getExchangeRate("BYN")
//                Log.d("ExchangeRates", "Response: ${response.body()?.conversionRates}")
//            }
//        } catch (e: Exception) {
//            Log.e("API", "Error", e)
//        }
        val expenseDatabase = ExpenseDatabase.getInstance(this)

        enableEdgeToEdge()
        setContent {
            SpendlyTheme {

                val sharedViewModel = viewModel<SharedViewModel>(
                    viewModelStoreOwner = LocalActivity.current as ComponentActivity)

                val expenseViewModel = viewModel<ExpenseViewModel>(
                    factory = ExpenseViewModelFactory(
                        expenseDatabase.expenseDao,
                        expenseDatabase.categoryDao,
                        sharedViewModel
                    )
                )

                val categoryViewModel = viewModel<CategoryViewModel>(
                    factory = CategoryViewModelFactory(
                        expenseDatabase.categoryDao,
                        sharedViewModel
                    )
                )

                val navController = rememberNavController()
                val backStackEntry = navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry.value?.destination

                val showAddingExpenseDialog = sharedViewModel.showExpenseDialog
                val showAddingCategoryDialog = sharedViewModel.showCategoryDialog

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
//                                showAddingExpenseDialog.value = true
                                sharedViewModel.updateShowExpenseDialog(true)
                            }
                            CategoriesScreenRoute::class.java -> FloatingActionButtonAdd {
//                                showAddingCategoryDialog.value = true
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
                        expenseDatabase = expenseDatabase,
                        sharedViewModel = sharedViewModel,
                        expenseViewModel = expenseViewModel,
                        categoryViewModel = categoryViewModel
                    )
                    AddingExpenseDialog(
                        showAddingExpenseDialog.value,
                        expenseViewModel
                    ) {
                        sharedViewModel.updateShowExpenseDialog(false)
                    }
                    AddingCategoryDialog(
                        showAddingCategoryDialog.value,
                        categoryViewModel
                    ) {
                        sharedViewModel.updateShowCategoryDialog(false)
                    }
                }
            }
        }
    }
}

