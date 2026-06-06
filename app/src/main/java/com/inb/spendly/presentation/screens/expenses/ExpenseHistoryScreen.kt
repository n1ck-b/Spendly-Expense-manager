package com.inb.spendly.presentation.screens.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.inb.spendly.R
import com.inb.spendly.data.models.relations.ExpenseWithCategory
import com.inb.spendly.presentation.components.DropDownMenu
import com.inb.spendly.presentation.screens.SharedViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale

@Composable
fun ExpenseHistoryScreen(
    paddingValues: PaddingValues,
    viewModel: ExpenseViewModel,
    sharedViewModel: SharedViewModel
) {

    val selectedDateRange = sharedViewModel.selectedDateRange

    val expensesWithCategories by viewModel.expensesList.collectAsState()

    val showActionDialog = remember { mutableStateOf(false) }

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
        ExpenseList(
            expensesWithCategories,
            onLongItemClick = {
                showActionDialog.value = true
                viewModel.updateSelectedExpenseId(it)
            }
        )
        ActionDialog(
            showDialog = showActionDialog.value,
            onDismissRequest = {
                showActionDialog.value = false
            },
            onEditButtonClicked = {
                sharedViewModel.updateShowExpenseDialog(true)
                viewModel.updateState()
                showActionDialog.value = false
            },
            onDeleteButtonClicked = {
                viewModel.deleteExpense()
                showActionDialog.value = false
            }
        )
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
            text = stringResource(R.string.drop_down_select_period),
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
fun ExpenseList(
    expensesWithCategory: List<ExpenseWithCategory>,
    onLongItemClick: (Long) -> Unit
) {
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
            ExpenseListItem(expenseWithCategory, onLongItemClick)
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
fun ExpenseListItem(
    item: ExpenseWithCategory,
    onLongItemClick: (Long) -> Unit
) {

    val formatter = SimpleDateFormat("dd.MM.yyyy", LocalLocale.current.platformLocale)

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    onLongItemClick(item.expense.id)
                }
            ),
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
                            modifier = Modifier.size(30.dp),
                            tint = Color(item.category.color)
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
                        if(item.expense.note != null && item.expense.note != "") {
                            Text(
                                text = item.expense.note!!
                            )
                        }
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
fun ActionDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit
) {
    if(showDialog) {
        Dialog(
            onDismissRequest = {
                onDismissRequest()
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(7.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(30.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 10.dp),
                        text = stringResource(R.string.choose_action_update_delete),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    TextButton(
                        onClick = onEditButtonClicked
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(35.dp),
                                painter = painterResource(R.drawable.outline_edit_square_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = stringResource(R.string.edit_button),
                                color = MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    TextButton(
                        onClick = onDeleteButtonClicked
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(35.dp),
                                painter = painterResource(R.drawable.outline_delete_24),
                                contentDescription = null,
                                tint = Color(0xFFC02929)
                            )
                            Text(
                                text = stringResource(R.string.delete_button),
                                color = Color(0xFFC02929),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}