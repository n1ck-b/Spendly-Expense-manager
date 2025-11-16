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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inb.spendly.R
import com.inb.spendly.models.relations.ExpenseWithCategory
import com.inb.spendly.ui.components.DropDownMenu
import com.inb.spendly.viewmodels.ExpenseViewModel
import com.inb.spendly.viewmodels.SharedViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ExpenseHistoryScreen(
    paddingValues: PaddingValues,
    viewModel: ExpenseViewModel,
    sharedViewModel: SharedViewModel
) {

    val selectedDateRange = sharedViewModel.selectedDateRange

    val expensesWithCategories by viewModel.expensesList.collectAsState()

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
        ExpenseHistoryScreenHeader()
        DateDropDown(sharedViewModel, selectedDateRange.collectAsState().value)
        ExpenseAmountTile(expensesWithCategories.sumOf { it.expense.amount.toDouble() }.toFloat())
        ExpenseList(expensesWithCategories)
    }
}

@Composable
fun ExpenseHistoryScreenHeader() {
    Text(
        text = stringResource(R.string.expense_history_screen_header),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(top = 20.dp, bottom = 15.dp)
    )
}

@Composable
fun DateDropDown(sharedViewModel: SharedViewModel, selectedDateRange: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select a period",
            style = MaterialTheme.typography.titleMedium
        )
        DropDownMenu(
            items = stringArrayResource(R.array.time_periods).toList(),
            onItemClick = {
                sharedViewModel.updateDateRange(it)
            },
            selectedItem = selectedDateRange
        )
    }
}

@Composable
fun ExpenseAmountTile(sumForSelectedPeriod: Float) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(15.dp),
    ) {
        Row(
            modifier = Modifier
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
                    text = "${BigDecimal(sumForSelectedPeriod.toDouble())
                        .setScale(2, RoundingMode.HALF_UP)} Br",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Composable
fun ExpenseList(expensesWithCategory: List<ExpenseWithCategory>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp)
    ) {
        if(expensesWithCategory.isEmpty()) {
            item {
                NoExpensesFound()
            }
        }
        items(expensesWithCategory) { expenseWithCategory ->
            ExpenseListItem(expenseWithCategory)
        }
    }
}

@Composable
fun NoExpensesFound() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(50.dp),
            painter = painterResource(R.drawable.outline_folder_open_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.no_expenses_for_selected_period),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ExpenseListItem(item: ExpenseWithCategory) {

    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(15.dp),
    ) {
        Row(
            modifier = Modifier
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
                            painter = painterResource(item.category.iconId),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        Column {
                            Text(
                                text = item.category.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = formatter.format(item.expense.date)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(7.dp))
                    Row {
                        Spacer(modifier = Modifier.width(40.dp))
                        Text(
                            text = item.expense.note
                        )
                    }
                }
            }
            Text(
                text = "-${BigDecimal(item.expense.amount.toDouble())
                    .setScale(2, RoundingMode.HALF_UP)} Br"
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ExpenseHistoryScreenPreview() {
//    ExpenseHistoryScreen(PaddingValues(30.dp))
}