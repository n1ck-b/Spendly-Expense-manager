package com.inb.spendly.presentation.screens.categories

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.inb.spendly.R
import com.inb.spendly.presentation.screens.UiEvent
import com.inb.spendly.presentation.ui.theme.CategoryColors
import com.inb.spendly.presentation.ui.theme.CategoryIcons

@Composable
fun AddingCategoryDialogContent(
    paddingValues: PaddingValues,
    onCancelButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit,
    viewModel: CategoryViewModel
) {
    val state = viewModel.state.collectAsState()

    val currentState = state.value

    if (currentState is CategoryState.Loaded
        && currentState.dialogState is CategoryDialogState.AddingCategory
    ) {
        val showErrors = remember { mutableStateOf(false) }

        val showIconDialog = remember { mutableStateOf(false) }
        val showColorDialog = remember { mutableStateOf(false) }

        val context = LocalContext.current
        val fillAllFieldsWarning = stringResource(R.string.warning_fill_all_fields)
        val categoryAlreadyExistsWarning = stringResource(R.string.category_warning_already_exists)

        LaunchedEffect(Unit) {
            viewModel.events.collect { event ->
                if (event == UiEvent.ShowToastNotAllFieldsFilled) {
                    Toast.makeText(
                        context,
                        fillAllFieldsWarning,
                        Toast.LENGTH_LONG
                    ).show()
                    showErrors.value = true
                } else if (event == UiEvent.ShowToastCategoryAlreadyExists) {
                    Toast.makeText(
                        context,
                        categoryAlreadyExistsWarning,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AddingCategoryScreenHeader()

            CategoryNameTextField(
                currentName = currentState.dialogState.name,
                onValueChanged = {
                    viewModel.processCommand(CategoryCommand.InputName(it))
                },
                onClearIconClick = {
                    viewModel.processCommand(CategoryCommand.InputName(""))
                },
                showErrors = showErrors.value
            )

            ChoosingIconCard(
                selectedIcon = currentState.dialogState.iconId,
                selectedColor = currentState.dialogState.color,
                onClick = {
                    showIconDialog.value = true
                }
            )
            ChoosingIconColorCard(
                selectedColor = currentState.dialogState.color,
                onClick = {
                    showColorDialog.value = true
                }
            )

            IconsListDialog(
                onDismissRequest = {
                    showIconDialog.value = false
                },
                categoryIcons = CategoryIcons.icons,
                showIconDialog = showIconDialog,
                onItemClicked = {
                    viewModel.processCommand(CategoryCommand.InputIconId(it))
                    showIconDialog.value = false
                }
            )

            ColorsListDialog(
                onDismissRequest = {
                    showColorDialog.value = false
                },
                colors = CategoryColors.colors,
                showColorDialog = showColorDialog,
                onItemClicked = {
                    viewModel.processCommand(CategoryCommand.InputColor(it))
                    showColorDialog.value = false
                }
            )

            CancelSaveButtons(
                onCancelButtonClicked = onCancelButtonClicked,
                onSaveButtonClicked = onSaveButtonClicked,
                saveButtonEnabled = currentState.dialogState.name.isNotBlank()
            )
        }
    }


}

@Composable
fun AddingCategoryScreenHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.category_dialog_header),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun CategoryNameTextField(
    currentName: String,
    onValueChanged: (String) -> Unit,
    onClearIconClick: () -> Unit,
    showErrors: Boolean
) {

    var textFieldValue by remember {
        mutableStateOf(
            if (currentName.isNotBlank() && currentName.isNotEmpty()) currentName
            else ""
        )
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
            onValueChanged(newValue)
        },
        label = {
            Text(
                text = stringResource(R.string.category_dialog_label_name),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        placeholder = {
            Text(
                text = stringResource(R.string.category_dialog_placeholder_name),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
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
        isError = (textFieldValue.isBlank() || textFieldValue.isEmpty()) && showErrors,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
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
fun ChoosingIconCard(
    selectedIcon: String,
    selectedColor: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.category_dialog_label_select_icon),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Icon(
                imageVector = CategoryIcons.getIconByKey(selectedIcon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp),
                tint = selectedColor
            )
        }
    }
}

@Composable
fun ChoosingIconColorCard(
    selectedColor: Color,
    onClick: () -> Unit
) {

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = RoundedCornerShape(10.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 20.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.category_dialog_label_select_icon_color),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(selectedColor)
            )
        }
    }
}

@Composable
fun IconsListDialog(
    onDismissRequest: () -> Unit,
    categoryIcons: List<CategoryIcons.CategoryIconItem>,
    showIconDialog: MutableState<Boolean>,
    onItemClicked: (String) -> Unit
) {
    if (showIconDialog.value) {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 80.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(
                        all = 24.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(categoryIcons) { currentIcon ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            onClick = { onItemClicked(currentIcon.key) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            ),
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxSize()
                                    .aspectRatio(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = currentIcon.icon,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(32.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColorsListDialog(
    onDismissRequest: () -> Unit,
    colors: List<Color>,
    showColorDialog: MutableState<Boolean>,
    onItemClicked: (Color) -> Unit
) {
    if (showColorDialog.value) {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 80.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    contentPadding = PaddingValues(
                        all = 24.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(colors) { color ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color)
                                .clickable { onItemClicked(color) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CancelSaveButtons(
    onCancelButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit,
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
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.dialog_button_cancel),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Button(
                enabled = saveButtonEnabled,
                onClick = onSaveButtonClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.dialog_button_save),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}