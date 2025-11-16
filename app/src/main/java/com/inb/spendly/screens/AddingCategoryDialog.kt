package com.inb.spendly.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AddingCategoryDialog(showDialog: MutableState<Boolean>) {
    if(showDialog.value) {
        Dialog(
            onDismissRequest = { showDialog.value = false }
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
                        showDialog.value = false
                    },
                    onSaveButtonClicked = {}
                )
            }
        }
    }
}