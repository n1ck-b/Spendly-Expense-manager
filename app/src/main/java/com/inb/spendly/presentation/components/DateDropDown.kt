package com.inb.spendly.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.inb.spendly.R
import com.inb.spendly.presentation.SharedCommand
import com.inb.spendly.presentation.screens.SharedViewModel

@Composable
fun DateDropDown(sharedViewModel: SharedViewModel, selectedDateRange: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.drop_down_select_period),
            style = MaterialTheme.typography.titleMedium
        )
        DropDownMenu(
            items = stringArrayResource(R.array.time_periods).toList(),
            onItemClick = {
                sharedViewModel.processCommand(SharedCommand.UpdateDateRange(it))
            },
            selectedItem = selectedDateRange
        )
    }
}