package com.inb.spendly.presentation.screens.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.inb.spendly.R
import com.inb.spendly.domain.entities.Category
import com.inb.spendly.presentation.ui.theme.CategoryIcons.getIconByKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesDropDownMenu(
    modifier: Modifier = Modifier,
    categories: List<Category>,
    onItemClick: (Category) -> Unit,
    selectedItem: Category
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedItem.name,
            leadingIcon = {
                Box(
                    modifier = Modifier.padding(start = 16.dp, end = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Color(selectedItem.color).copy(alpha = 0.15f)
                            )
                            .padding(8.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp),
                            imageVector = getIconByKey(selectedItem.iconId),
                            contentDescription = "Expense category icon",
                            tint = Color(selectedItem.color)
                        )
                    }
                }
            },
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = modifier
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
            ),
            textStyle = MaterialTheme.typography.bodyLarge,
            shape = RoundedCornerShape(10.dp),
            label = {
                Text(
                    text = stringResource(R.string.expense_dialog_label_category),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer),
            shape = RoundedCornerShape(10.dp)
        ) {
            categories.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer),
                    text = {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Color(item.color).copy(alpha = 0.15f)
                                )
                                .padding(8.dp)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(24.dp),
                                imageVector = getIconByKey(item.iconId),
                                contentDescription = "Expense category icon",
                                tint = Color(item.color)
                            )
                        }
                    },
                    onClick = {
                        onItemClick(item)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}