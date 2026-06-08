package com.inb.spendly.presentation.screens.expenses

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.inb.spendly.R
import com.inb.spendly.domain.Utils.hasInternetConnection
import com.inb.spendly.presentation.screens.UiEvent

@Composable
fun AddingExpenseDialog(
    expenseViewModel: ExpenseViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit
) {

    val context = LocalContext.current.applicationContext

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(7.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            AddingExpenseDialogContent(
                paddingValues = PaddingValues(30.dp),
                onCancelButtonClicked = {
                    onDismissRequest()
                },
                onSaveButtonClicked = {
                    if (hasInternetConnection(context)) {
                        expenseViewModel.processCommand(ExpenseCommand.SaveExpense)
                    } else {
                        Toast.makeText(
                            context,
                            R.string.no_internet_connection,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                viewModel = expenseViewModel
            )
        }
    }
}