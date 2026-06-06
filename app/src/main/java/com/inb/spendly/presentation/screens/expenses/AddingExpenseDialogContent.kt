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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.inb.spendly.R
import com.inb.spendly.data.models.Currencies
import com.inb.spendly.domain.entities.Category
import com.inb.spendly.presentation.components.DatePickerModal
import com.inb.spendly.presentation.components.DropDownMenu
import com.inb.spendly.presentation.screens.UiEvent
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun AddingExpenseDialogContent(
    paddingValues: PaddingValues,
    onCancelButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit,
    viewModel: ExpenseViewModel
) {

    val fillAllFieldsWarning = stringResource(R.string.fill_all_fields_warning)

    val errorGettingExchangeRatesWarning = stringResource(R.string.error_getting_exchange_rates)

    val context = LocalContext.current

    val showErrors = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event == UiEvent.ShowToastNotAllFieldsFilled) {
                Toast.makeText(context, fillAllFieldsWarning, Toast.LENGTH_LONG).show()
                showErrors.value = true
            }
            else if(event == UiEvent.ShowToastErrorGettingExchangeRates) {
                Toast.makeText(context, errorGettingExchangeRatesWarning, Toast.LENGTH_LONG).show()
            }
        }
    }

    val expenseState by viewModel.state.collectAsState()

    val categories by viewModel.categoriesList.collectAsState(emptyList())

    val selectedCurrency by viewModel.selectedCurrency

    val showDatePicker = remember { mutableStateOf(false) }

    val formattedDate =
    if(expenseState.date != null)
        SimpleDateFormat("dd.MM.yyyy", LocalLocale.current.platformLocale)
            .format(expenseState.date ?: Date())
    else ""

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        AddingExpenseScreenHeader()
        ExpenseCurrencyDropDown(
            selectedCurrency = selectedCurrency,
            onItemClick = {
                viewModel.updateSelectedCurrency(it)
            }
        )
        AmountTextField (
            currentAmount = expenseState.amount,
            onValueChanged = {
                viewModel.updateExpenseAmount(it.toFloatOrNull() ?: 0f)
            },
            onClearIconClick = {
                viewModel.updateExpenseAmount(0f)
            },
            showErrors = showErrors.value
        )
        DateTextField(
            formattedDate,
            showDatePicker,
            onDateSelected = {
                 viewModel.updateExpenseDate(it)
                showDatePicker.value = false
            },
            onDismiss = {
                showDatePicker.value = false
            },
            onClearIconClick = {
                viewModel.updateExpenseDate(null)
            },
            showErrors = showErrors.value
        )
        NoteTextField(
            currentNote = expenseState.note,
            onValueChanged = {
                viewModel.updateExpenseNote(it)
            },
            onClearIconClick = {
                viewModel.updateExpenseNote(null)
            }
        )
        ExpenseCategoryDropDown(
            categories = categories,
            onItemClick = {
                viewModel.updateExpenseCategoryName(it)
            },
            selectedCategory = expenseState.categoryName ?: ""
        )
        CancelSaveButtons(onCancelButtonClicked, onSaveButtonClicked)

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
            if (currentAmount == 0f) ""
        else if (currentAmount % 1 == 0f)
            currentAmount.toInt().toString()
        else
            currentAmount.toString()
        )
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue,
        onValueChange = {
            onValueChanged(it)
            textFieldValue = it
        },
        label = {
            Text(
                text = stringResource(R.string.adding_expense_screen_amount_text_field_label)
            )
        },
        placeholder = {
            Text(
                text = stringResource(R.string.adding_expense_screen_amount_text_field_placeholder)
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
                    modifier = Modifier
                        .clickable {
                            textFieldValue = ""
                            onClearIconClick()
                        }
                )
            }
        },
        shape = RoundedCornerShape(7.dp),
        isError = (textFieldValue.isBlank() || textFieldValue.isEmpty()) && showErrors
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

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        interactionSource = interactionSource,
        value = formattedDate,
        onValueChange = {},
        label = {
            Text(
                text = stringResource(R.string.adding_expense_screen_date_text_field_label)
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
        shape = RoundedCornerShape(7.dp),
        isError = (formattedDate.isBlank() || formattedDate.isEmpty()) && showErrors
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
fun ExpenseCategoryDropDown(
    categories: List<Category>,
    onItemClick: (String) -> Unit,
    selectedCategory: String
) {

    val categoriesNames =
    if(categories.isNotEmpty())
        categories.map { it.name }
    else
        emptyList()

    Column(
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                    append(stringResource(R.string.adding_expense_screen_category_dropdown_label))

                    withStyle(style = SpanStyle(color = Color.Red)) {
                        append(" *")
                    }
            },
            style = MaterialTheme.typography.titleMedium
        )
        DropDownMenu(
            items = categoriesNames,
            onItemClick = onItemClick,
            selectedItem = selectedCategory
        )
    }
}

@Composable
fun ExpenseCurrencyDropDown(
    selectedCurrency: Currencies,
    onItemClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = stringResource(R.string.adding_expense_screen_currency_dropdown_label),
            style = MaterialTheme.typography.titleMedium
        )
        DropDownMenu(
            items = Currencies.entries.map { it.name },
            onItemClick = onItemClick,
            selectedItem = selectedCurrency.toString()
        )
    }
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

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
            onValueChanged(newValue)
        },
        label = {
            Text(
                text = stringResource(R.string.adding_expense_screen_note_text_field_label)
            )
        },
        placeholder = {
            Text(
                text = stringResource(R.string.adding_expense_screen_note_text_field_placeholder)
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
        shape = RoundedCornerShape(7.dp)
    )
}

@Composable
fun CancelSaveButtons(
    onCancelButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = onCancelButtonClicked
            ) {
                Text(
                    text = stringResource(R.string.cancel_button),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Button(
                onClick = onSaveButtonClicked
            ) {
                Text(
                    text = stringResource(R.string.save_button)
                )
            }
        }
    }
}