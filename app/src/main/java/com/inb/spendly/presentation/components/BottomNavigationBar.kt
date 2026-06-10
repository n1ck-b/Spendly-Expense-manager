package com.inb.spendly.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
        modifier = Modifier
            .padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
            .clip(RoundedCornerShape(16.dp))
            .height(72.dp),
        windowInsets = WindowInsets(0, 0, 0, 0),
        contentColor = MaterialTheme.colorScheme.onSurface,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 2.dp
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