package com.inb.spendly.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<BottomNavBarItem>
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry.value?.destination

    NavigationBar(
        contentColor = MaterialTheme.colorScheme.onBackground,
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 5.dp
    ) {
        items.forEach { item ->
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
            Text(text = item.name)
        },
        icon = {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(item.icon),
                contentDescription = null
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onBackground,
            selectedTextColor = MaterialTheme.colorScheme.onBackground,
            unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
            unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
        )
    )
}