package com.inb.spendly.presentation.screens.categories

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun AddingCategoryDialog(
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit,
    onSaveButtonClicked: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            AddingCategoryDialogContent(
                paddingValues = PaddingValues(32.dp),
                onCancelButtonClicked = onDismissRequest,
                onSaveButtonClicked = onSaveButtonClicked,
                viewModel = categoryViewModel
            )
        }
    }
}