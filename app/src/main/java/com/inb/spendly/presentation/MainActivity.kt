package com.inb.spendly.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.inb.spendly.R
import com.inb.spendly.presentation.components.BottomNavBarItem
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

                NavGraph(
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
        }
    }
}