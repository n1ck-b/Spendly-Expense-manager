package com.inb.spendly.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inb.spendly.R
import com.inb.spendly.ui.components.DropDownMenu
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ExpenseHistoryScreen(paddingValues: PaddingValues) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(
                top = paddingValues.calculateTopPadding() + 20.dp,
                bottom = paddingValues.calculateBottomPadding(),
                start = 40.dp,
                end = 40.dp
            )
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        DateDropDown()
        ExpenseAmountTile()
        ExpenseList()
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DropDownMenu(items: List<String>) {
//    //val timeIntervals = stringArrayResource(R.array.time_periods).toList()
//
//    var expanded by remember  {
//        mutableStateOf(false)
//    }
//
//    var selectedItem by remember {
//        mutableStateOf(items[0])
//    }
//
//    Row (
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            imageVector = Icons.Outlined.DateRange,
//            contentDescription = null,
//            modifier = Modifier.size(30.dp)
//        )
//        ExposedDropdownMenuBox(
//            expanded = expanded,
//            onExpandedChange = { expanded = !expanded }
//        ) {
//            TextField(
//                value = selectedItem,
//                onValueChange = {},
//                readOnly = true,
//                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
//                modifier = Modifier
//                    .menuAnchor(
//                        type = MenuAnchorType.PrimaryNotEditable,
//                        enabled = true
//                    )
//                    .width(170.dp),
//                colors = TextFieldDefaults.colors(
//                    focusedContainerColor = MaterialTheme.colorScheme.background,
//                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
//                    unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
//                    focusedIndicatorColor = MaterialTheme.colorScheme.background,
//                )
//
//            )
//            ExposedDropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false },
//                modifier = Modifier.background(MaterialTheme.colorScheme.background)
//            ) {
//                items.forEach { item ->
//                    DropdownMenuItem(
//                        text = { Text(text = item) },
//                        onClick = {
//                            selectedItem = item
//                            expanded = false
//                        },
//                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
//                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
//                    )
//                }
//            }
//        }
//    }
//}

@Composable
fun DateDropDown() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(30.dp),
            imageVector = Icons.Outlined.DateRange,
            contentDescription = null,
        )
        DropDownMenu(stringArrayResource(R.array.time_periods).toList())
    }
}

@Composable
fun ExpenseAmountTile() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(Color.LightGray)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.expense_amount_header),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "700 Br",
            )
        }
    }
}

@Composable
fun ExpenseList() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(top = 40.dp)
    ) {
        items(20) {
            ExpenseListItem()
        }
    }
}

@Composable
fun ExpenseListItem(/*expense: Expense*/) {

    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(Color.LightGray)
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.size(10.dp))
                    Column {
                        Text(
                            text = "Home",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = formatter.format(Date())
                        )
                    }
                }
                Spacer(modifier = Modifier.size(7.dp))
                Row {
                    Spacer(modifier = Modifier.width(40.dp))
                    Text(
                        text = "Comment"
                    )
                }
            }
        }
        Text(
            text = "-100 Br"
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ExpenseHistoryScreenPreview() {
    ExpenseHistoryScreen(PaddingValues(30.dp))
}