package com.inb.spendly.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.inb.spendly.presentation.navigation.NavGraph
import com.inb.spendly.presentation.screens.SharedViewModel
import com.inb.spendly.presentation.ui.theme.SpendlyTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

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