package com.inb.spendly.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.inb.spendly.R
import com.inb.spendly.presentation.navigation.CategoriesScreenRoute
import com.inb.spendly.presentation.navigation.ExpenseHistoryScreenRoute
import com.inb.spendly.presentation.navigation.StatisticsScreenRoute

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry.value?.destination

    val bottomNavBarItems = listOf(
        BottomNavBarItem(
            route = CategoriesScreenRoute,
            name = stringResource(R.string.category_screen_name),
            icon = Icons.Outlined.Dashboard
        ),
        BottomNavBarItem(
            route = ExpenseHistoryScreenRoute,
            name = stringResource(R.string.expense_screen_name),
            icon = Icons.AutoMirrored.Outlined.FormatListBulleted
        ),
        BottomNavBarItem(
            route = StatisticsScreenRoute,
            name = stringResource(R.string.statistics_screen_name),
            icon = Icons.Outlined.BarChart
        )
    )


    NavigationBar(
        modifier = Modifier
            .padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(72.dp),
        windowInsets = WindowInsets(0, 0, 0, 0),
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 2.dp
    ) {
        bottomNavBarItems.forEach { item ->
            BottomNavBarItem(navController, item, currentDestination)
        }
    }

}

@Composable
fun RowScope.BottomNavBarItem(
    navController: NavHostController,
    item: BottomNavBarItem,
    currentDestination: NavDestination?
) {

    val currentDestinationClass = currentDestination?.route?.let { Class.forName(it) }
    val selected = item.route::class.java == currentDestinationClass

    NavigationBarItem(
        selected = selected,
        onClick = {
            navController.navigate(item.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        },
        label = {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        icon = {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = item.icon,
                contentDescription = null
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
            indicatorColor = Color.Transparent
        )
    )
}