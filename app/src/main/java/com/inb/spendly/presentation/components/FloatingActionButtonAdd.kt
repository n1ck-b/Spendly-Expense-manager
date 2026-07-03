package com.inb.spendly.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun FloatingActionButtonAdd(onClick: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier.clip(
            RoundedCornerShape(16.dp)
        ).padding(end = 6.dp, bottom = 74.dp),
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp)
    ) {
        Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add")
    }
}