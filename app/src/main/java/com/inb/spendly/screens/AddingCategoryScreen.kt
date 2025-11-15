package com.inb.spendly.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.inb.spendly.R
import com.inb.spendly.ui.theme.CategoryColors
import com.inb.spendly.ui.theme.CategoryIcons
import com.inb.spendly.ui.theme.DefaultIconColor

@Composable
fun AddingCategoryScreen(paddingValues: PaddingValues) {

    val selectedIcon = remember {
        mutableIntStateOf(R.drawable.outline_image_24)
    }

    val selectedColor = remember {
        mutableStateOf(DefaultIconColor)
    }

    val showIconDialog = remember { mutableStateOf(false) }
    val showColorDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(
                top = paddingValues.calculateTopPadding() + 20.dp,
                bottom = paddingValues.calculateBottomPadding(),
                start = 40.dp,
                end = 40.dp
            )
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        AddingCategoryScreenHeader()
        CategoryNameTextField()
        ChoosingIconCard(selectedIcon,
            selectedColor
        ) { showIconDialog.value = true }
        ChoosingIconColorCard(selectedColor) { showColorDialog.value = true }
        IconsListDialog(
            onDismissRequest = { showIconDialog.value = false },
            categoryIcons = CategoryIcons.icons,
            showIconDialog = showIconDialog,
            onItemClicked = {
                selectedIcon.intValue = it
                showIconDialog.value = false
            }
        )
        ColorsListDialog(
            onDismissRequest = { showColorDialog.value = false },
            colors = CategoryColors.colors,
            showColorDialog = showColorDialog,
            onItemClicked = {
                selectedColor.value = it
                showColorDialog.value = false
            }
        )
    }
}

@Composable
fun AddingCategoryScreenHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.adding_category_screen_header),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun CategoryNameTextField() {

    var textFieldValue by remember {
        mutableStateOf("")
    }

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = textFieldValue,
        onValueChange = { newValue ->
            textFieldValue = newValue
        },
        label = {
            Text(
                text = stringResource(R.string.adding_category_screen_name_text_field_label)
            )
        },
        placeholder = {
            Text(
                text = stringResource(R.string.adding_category_screen_name_text_field_placeholder)
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
                        .clickable { textFieldValue = "" }
                )
            }
        },
        shape = RoundedCornerShape(7.dp)
    )
}

@Composable
fun ChoosingIconCard(selectedIcon: MutableState<Int>, selectedColor: MutableState<Color>, onClick: () -> Unit) {

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(7.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.adding_category_screen_select_icon),
                style = MaterialTheme.typography.bodyLarge,
            )
            Icon(
                painter = painterResource(selectedIcon.value),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp),
                tint = selectedColor.value
            )
        }
    }
}

@Composable
fun ChoosingIconColorCard(selectedColor: MutableState<Color>, onClick: () -> Unit) {

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        shape = RoundedCornerShape(7.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 15.dp,
                    bottom = 15.dp,
                    start = 15.dp,
                    end = 19.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.adding_category_screen_select_icon_color),
                style = MaterialTheme.typography.bodyLarge,
            )
            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .background(selectedColor.value)
            )
//            Canvas(
//                modifier = Modifier
//                    .size(40.dp)
//            ) {
//                drawRect(
//                    size = size,
//                    color = Color.Black
//                )
//            }
        }
    }
}

@Composable
fun IconsListDialog(onDismissRequest: () -> Unit, categoryIcons: List<Int>,
                    showIconDialog: MutableState<Boolean>, onItemClicked: (Int) -> Unit) {
    if(showIconDialog.value) {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(7.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(
                        top = 30.dp,
                        bottom = 30.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(categoryIcons) { icon ->
                        OutlinedCard (
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(7.dp),
                            onClick = { onItemClicked(icon) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(7.dp)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(icon),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp),
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
fun ColorsListDialog(onDismissRequest: () -> Unit, colors: List<Color>,
                    showColorDialog: MutableState<Boolean>, onItemClicked: (Color) -> Unit) {
    if(showColorDialog.value) {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(7.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    contentPadding = PaddingValues(
                        top = 30.dp,
                        bottom = 30.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(colors) { color ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(7.dp))
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
@Preview(showBackground = true, showSystemUi = true)
fun AddingCategoryScreenPreview() {
    AddingCategoryScreen(PaddingValues(30.dp))
}