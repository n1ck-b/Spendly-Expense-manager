package com.inb.spendly.screens

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inb.spendly.R
import com.inb.spendly.models.Currencies
import com.inb.spendly.ui.components.DatePickerModal
import com.inb.spendly.ui.components.DropDownMenu
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddingExpenseDialogContent(
    paddingValues: PaddingValues,
    onCancelButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit
) {

    val showDatePicker = remember { mutableStateOf(false) }

    var selectedDate by remember { mutableStateOf<Long?>(null) }

//    var formattedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date())

    var formattedDate: String

    if (selectedDate != null) {
        val date = Date(selectedDate!!)
        formattedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
    } else
        formattedDate = ""

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        AddingExpenseScreenHeader()
        ExpenseCurrencyDropDown()
        AmountTextField()
        DateTextField(
            formattedDate,
            showDatePicker,
            onDateSelected = {
                selectedDate = it
                showDatePicker.value = false
            },
            onDismiss = {
                showDatePicker.value = false
            },
            onClearIconClick = { selectedDate = null }
        )
        NoteTextField()
        ExpenseCategoryDropDown()
        CancelSaveButtons(onCancelButtonClicked, onSaveButtonClicked)

//        when {
//            showDatePicker ->
//                DatePickerModal(
//                    onDateSelected = {
//                        selectedDate = it
//                        showDatePicker = false
//                    },
//                    onDismiss = {
//                        showDatePicker = false
//                    }
//                )
//        }

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
fun AmountTextField() {

    var textFieldValue by remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
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
                        .clickable { textFieldValue = "" }
                )
            }
        },
        shape = RoundedCornerShape(7.dp)
    )
}

@Composable
fun DateTextField(
    formattedDate: String, showDatePicker: MutableState<Boolean>, onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit, onClearIconClick: () -> Unit
) {

    val interactionSource = remember {
        MutableInteractionSource()
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            /*.clickable { showDatePicker.value = true }*/,
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
        shape = RoundedCornerShape(7.dp)
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
fun ExpenseCategoryDropDown() {
    Column(
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = stringResource(R.string.adding_expense_screen_category_dropdown_label),
            style = MaterialTheme.typography.titleMedium
        )
        DropDownMenu(
            listOf(
                "Food",
                "Family",
                "Car",
                "Sport"
            ),
            {},
            ""
        )
    }
}

@Composable
fun ExpenseCurrencyDropDown() {
    Column(
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = stringResource(R.string.adding_expense_screen_currency_dropdown_label),
            style = MaterialTheme.typography.titleMedium
        )
        DropDownMenu(Currencies.entries.map { it.name }, {}, "")
    }
}

@Composable
fun NoteTextField() {

    var textFieldValue by remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
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
                        .clickable { textFieldValue = "" }
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
                    text = stringResource(R.string.cancel_button)
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

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun AddingExpenseDialogContentPreview() {
    AddingExpenseDialogContent(PaddingValues(30.dp), {}, {})
}