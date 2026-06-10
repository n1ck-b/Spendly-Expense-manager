package com.inb.spendly.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.inb.spendly.presentation.FilterType
import com.inb.spendly.presentation.screens.SharedCommand
import com.inb.spendly.presentation.screens.SharedViewModel

@Composable
fun DateChips(
    sharedViewModel: SharedViewModel
) {

    val selectedFilterType = sharedViewModel.selectedFilterType.collectAsState()

    LazyRow (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        FilterType.entries.forEach { filter ->
            item {
                DateFilterChip(
                    dateRange = filter,
                    onClick = {
                        sharedViewModel.processCommand(SharedCommand.UpdateDateRange(filter))
                    },
                    selected = selectedFilterType.value == filter
                )
            }
        }
    }
}

@Composable
private fun DateFilterChip(
    dateRange: FilterType,
    onClick: () -> Unit,
    selected: Boolean
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = stringResource(dateRange.titleResourceId),
                style = MaterialTheme.typography.titleSmall
            )
        },
        leadingIcon = {
            if (selected) {
                Icon(
                    modifier = Modifier.size(15.dp),
                    imageVector = Icons.Outlined.Done,
                    contentDescription = "Done icon"
                )
            }
        },
        shape = CircleShape,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            labelColor = MaterialTheme.colorScheme.onSurface,
        ),
        border = BorderStroke(width = 0.dp, color = Color.Transparent)
    )
}