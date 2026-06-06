package com.inb.spendly.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inb.spendly.R
import com.inb.spendly.data.repository.ExpenseDatabase
import com.inb.spendly.presentation.navigation.CategoriesScreenRoute
import com.inb.spendly.presentation.navigation.ExpenseHistoryScreenRoute
import com.inb.spendly.presentation.navigation.NavGraph
import com.inb.spendly.presentation.navigation.StatisticsScreenRoute
import com.inb.spendly.presentation.components.BottomNavBarItem
import com.inb.spendly.presentation.components.BottomNavigationBar
import com.inb.spendly.presentation.components.FloatingActionButtonAdd
import com.inb.spendly.presentation.components.AddingCategoryDialog
import com.inb.spendly.presentation.components.AddingExpenseDialog
import com.inb.spendly.presentation.ui.theme.SpendlyTheme
import com.inb.spendly.presentation.screens.categories.CategoryViewModel
import com.inb.spendly.presentation.screens.categories.CategoryViewModelFactory
import com.inb.spendly.presentation.screens.expenses.ExpenseViewModel
import com.inb.spendly.presentation.screens.expenses.ExpenseViewModelFactory
import com.inb.spendly.presentation.screens.SharedViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val expenseDatabase = ExpenseDatabase.Companion.getInstance(this)

        enableEdgeToEdge()
        setContent {
            SpendlyTheme {

                val sharedViewModel = viewModel<SharedViewModel>(
                    viewModelStoreOwner = LocalActivity.current as ComponentActivity
                )

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
                        expenseDatabase.expenseDao,
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
                        when (currentDestination?.route?.let { Class.forName(it) }) {
                            ExpenseHistoryScreenRoute::class.java -> FloatingActionButtonAdd {
                                sharedViewModel.updateShowExpenseDialog(true)
                            }

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