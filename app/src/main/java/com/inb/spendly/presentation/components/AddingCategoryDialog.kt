package com.inb.spendly.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.inb.spendly.presentation.screens.categories.CategoryViewModel
import com.inb.spendly.presentation.screens.UiEvent

@Composable
fun AddingCategoryDialog(
    showDialog: Boolean,
    categoryViewModel: CategoryViewModel,
    onDismissRequest: () -> Unit
) {
    if(showDialog) {

        LaunchedEffect(Unit) {
            categoryViewModel.events.collect { event ->
                if (event == UiEvent.CloseDialog) onDismissRequest()
            }
        }

        Dialog(
            onDismissRequest = {
                onDismissRequest()
                categoryViewModel.resetValues()
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(7.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                AddingCategoryDialogContent(
                    paddingValues = PaddingValues(30.dp),
                    onCancelButtonClicked = {
                        onDismissRequest()
                        categoryViewModel.resetValues()
                    },
                    onSaveButtonClicked = {
                        categoryViewModel.saveCategory()
                    },
                    viewModel = categoryViewModel
                )
            }
        }
    }
}