package com.inb.spendly.presentation.screens.expenses

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.inb.spendly.R
import com.inb.spendly.data.models.Currencies
import com.inb.spendly.presentation.components.DatePickerModal
import com.inb.spendly.presentation.screens.UiEvent
import java.text.DateFormat
import java.text.SimpleDateFormat

@Composable
fun AddingExpenseDialogContent(
    paddingValues: PaddingValues,
    onCancelButtonClicked: () -> Unit,
    onSaveButtonClicked: (Boolean) -> Unit,
    viewModel: ExpenseViewModel
) {
    val state = viewModel.state.collectAsState()
    val currentState = state.value

    if (currentState is ExpenseState.Loaded
        && currentState.dialogState is ExpenseDialogState.AddingExpense
    ) {

        val fillAllFieldsWarning = stringResource(R.string.fill_all_fields_warning)

        val errorGettingExchangeRatesWarning = stringResource(R.string.error_getting_exchange_rates)

        val context = LocalContext.current.applicationContext

        val showErrors = remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {

            viewModel.events.collect { event ->
                if (event == UiEvent.ShowToastNotAllFieldsFilled) {
                    Toast.makeText(
                        context,
                        fillAllFieldsWarning,
                        Toast.LENGTH_LONG
                    ).show()
                    showErrors.value = true
                } else if (event == UiEvent.ShowToastErrorGettingExchangeRates) {
                    Toast.makeText(
                        context,
                        errorGettingExchangeRatesWarning,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }

        val showDatePicker = remember { mutableStateOf(false) }

        val formattedDate =
            if (currentState.dialogState.date != null)
                SimpleDateFormat.getDateInstance(DateFormat.SHORT)
                    .format(currentState.dialogState.date)
            else ""

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AddingExpenseScreenHeader()

            CurrencyDropDownMenu(
                onItemClick = {
                    viewModel.processCommand(ExpenseCommand.InputSelectedCurrency(it))
                },
                selectedItem = currentState.dialogState.selectedCurrency
            )

            AmountTextField(
                currentAmount = currentState.dialogState.amount,
                onValueChanged = {
                    viewModel.processCommand(ExpenseCommand.InputAmount(it.toFloatOrNull() ?: 0f))
                },
                onClearIconClick = {
                    viewModel.processCommand(ExpenseCommand.InputAmount(0f))
                },
                showErrors = showErrors.value
            )

            DateTextField(
                formattedDate = formattedDate,
                showDatePicker = showDatePicker,
                onDateSelected = {
                    viewModel.processCommand(ExpenseCommand.InputDate(it))
                    showDatePicker.value = false
                },
                onDismiss = {
                    showDatePicker.value = false
                },
                onClearIconClick = {
                    viewModel.processCommand(
                        ExpenseCommand.InputDate(null)
                    )
                },
                showErrors = showErrors.value
            )

            NoteTextField(
                currentNote = currentState.dialogState.note,
                onValueChanged = {
                    viewModel.processCommand(
                        ExpenseCommand.InputNote(it)
                    )
                },
                onClearIconClick = {
                    viewModel.processCommand(
                        ExpenseCommand.InputNote(null)
                    )
                }
            )

            CategoriesDropDownMenu(
                modifier = Modifier.fillMaxWidth(),
                onItemClick = {
                    viewModel.processCommand(
                        ExpenseCommand.InputCategory(it)
                    )
                },
                selectedItem = currentState.dialogState.category,
                categories = currentState.dialogState.categories
            )

            CancelSaveButtons(
                onCancelButtonClicked = onCancelButtonClicked,
                onSaveButtonClicked = onSaveButtonClicked,
                selectedCurrency = currentState.dialogState.selectedCurrency,
                saveButtonEnabled = currentState.dialogState.date != null
                        && currentState.dialogState.amount != 0f
            )
        }
    }
}

@Composable
fun AddingExpenseScreenHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.adding_expense_screen_header),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun AmountTextField(
    currentAmount: Float,
    onValueChanged: (String) -> Unit,
    onClearIconClick: () -> Unit,
    showErrors: Boolean
) {

    var textFieldValue by remember(currentAmount) {
        mutableStateOf(
            if (currentAmount % 1 == 0f)
                currentAmount.toInt().toString()
            else
                currentAmount.toString()
        )
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue,
        onValueChange = {
            onValueChanged(it)
            textFieldValue = it
        },
        label = {
            Text(
                text = stringResource(R.string.adding_expense_screen_amount_text_field_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        placeholder = {
            Text(
                text = stringResource(R.string.adding_expense_screen_amount_text_field_placeholder),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        trailingIcon = {
            if (textFieldValue != "") {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        textFieldValue = ""
                        onClearIconClick()
                    }
                )
            }
        },
        shape = RoundedCornerShape(10.dp),
        isError = textFieldValue.isBlank() && showErrors,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            errorContainerColor = MaterialTheme.colorScheme.errorContainer
        ),
        textStyle = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun DateTextField(
    formattedDate: String,
    showDatePicker: MutableState<Boolean>,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
    onClearIconClick: () -> Unit,
    showErrors: Boolean
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth(),
        interactionSource = interactionSource,
        value = formattedDate,
        onValueChange = {},
        label = {
            Text(
                text = stringResource(R.string.adding_expense_screen_date_text_field_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        readOnly = true,
        trailingIcon = {
            if (formattedDate != "") {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { onClearIconClick() }
                )
            }
        },
        shape = RoundedCornerShape(10.dp),
        isError = formattedDate.isBlank() && showErrors,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            errorContainerColor = MaterialTheme.colorScheme.errorContainer
        )
    )

    when {
        showDatePicker.value -> DatePickerModal(
            onDateSelected, onDismiss
        )
    }

    if (interactionSource.collectIsPressedAsState().value)
        showDatePicker.value = true
}

@Composable
fun NoteTextField(
    currentNote: String?,
    onValueChanged: (String) -> Unit,
    onClearIconClick: () -> Unit
) {

    var textFieldValue by remember(currentNote) {
        mutableStateOf(currentNote ?: "")
    }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
            onValueChanged(newValue)
        },
        label = {
            Text(
                text = stringResource(R.string.adding_expense_screen_note_text_field_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        placeholder = {
            Text(
                text = stringResource(R.string.adding_expense_screen_note_text_field_placeholder),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        maxLines = 2,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text
        ),
        trailingIcon = {
            if (textFieldValue != "") {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            textFieldValue = ""
                            onClearIconClick()
                        }
                )
            }
        },
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            errorContainerColor = MaterialTheme.colorScheme.errorContainer
        )
    )
}

@Composable
fun CancelSaveButtons(
    onCancelButtonClicked: () -> Unit,
    onSaveButtonClicked: (Boolean) -> Unit,
    selectedCurrency: Currencies,
    saveButtonEnabled: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = onCancelButtonClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.cancel_button),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Button(
                enabled = saveButtonEnabled,
                onClick = {
                    onSaveButtonClicked(selectedCurrency == Currencies.BYN)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.save_button),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}