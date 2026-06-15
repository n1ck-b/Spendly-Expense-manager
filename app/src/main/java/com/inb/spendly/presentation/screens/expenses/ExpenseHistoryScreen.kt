package com.inb.spendly.presentation.screens.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.MoneyOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.inb.spendly.R
import com.inb.spendly.domain.entities.ExpenseWithCategory
import com.inb.spendly.presentation.components.ActionDialog
import com.inb.spendly.presentation.components.BottomNavigationBar
import com.inb.spendly.presentation.components.DateChips
import com.inb.spendly.presentation.components.FloatingActionButtonAdd
import com.inb.spendly.presentation.screens.SharedViewModel
import com.inb.spendly.presentation.ui.theme.CategoryIcons.getIconByKey
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormat
import java.text.SimpleDateFormat

@Composable
fun ExpenseHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: ExpenseViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {

    val state = viewModel.state.collectAsState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButtonAdd(
                    onClick = {
                        viewModel.processCommand(ExpenseCommand.AddExpense)
                    }
                )
            },
        ) { paddingValues ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding() + 8.dp,
                    )
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExpenseHistoryScreenHeader()
                Spacer(Modifier.size(4.dp))
                DateChips(
                    sharedViewModel = sharedViewModel
                )
                when (val currentState = state.value) {
                    is ExpenseState.Loaded -> {

                        ExpensesStatisticsTile(
                            sumForSelectedPeriod = currentState.expenses
                                .sumOf { it.expense.amount.toDouble() }.toFloat(),
                            amountOfRecords = currentState.expenses.size
                        )
                        ExpenseList(
                            currentState.expenses,
                            onLongItemClick = {
                                viewModel.processCommand(ExpenseCommand.SelectAction(it))
                            }
                        )

                        when (currentState.dialogState) {

                            is ExpenseDialogState.AddingExpense -> {
                                AddingExpenseDialog(
                                    onDismissRequest = {
                                        viewModel.processCommand(ExpenseCommand.ReturnToList)
                                    }
                                )
                            }

                            ExpenseDialogState.Closed -> {}

                            is ExpenseDialogState.SelectingAction -> {
                                ActionDialog(
                                    onDismissRequest = {
                                        viewModel.processCommand(ExpenseCommand.ReturnToList)
                                    },
                                    onEditButtonClicked = {
                                        viewModel.processCommand(ExpenseCommand.EditExpense)
                                    },
                                    onDeleteButtonClicked = {
                                        viewModel.processCommand(
                                            ExpenseCommand.DeleteExpense(
                                                currentState.dialogState.expenseId
                                            )
                                        )
                                    }
                                )
                            }

                        }
                    }

                    ExpenseState.Loading -> {
                        // TODO
                    }
                }
            }
        }
        BottomNavigationBar(navController)
    }
}

@Composable
fun ExpenseHistoryScreenHeader() {
    Text(
        text = stringResource(R.string.expense_history_screen_header),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp)
    )
}

@Composable
fun ExpensesStatisticsTile(
    sumForSelectedPeriod: Float,
    amountOfRecords: Int
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(10.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${
                        BigDecimal(sumForSelectedPeriod.toDouble())
                            .setScale(2, RoundingMode.HALF_UP)
                    } Br",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = stringResource(R.string.expense_amount_header),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }

        Card(
            modifier = Modifier,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(10.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$amountOfRecords",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = stringResource(R.string.expense_records_amount_header),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall
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

    if (expensesWithCategory.isEmpty()) {
        NoExpensesFound()
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(
            top = 20.dp,
            bottom = 20.dp + 120.dp,
            start = 24.dp,
            end = 24.dp
        )
    ) {
        items(expensesWithCategory) { expenseWithCategory ->
            ExpenseListItem(expenseWithCategory, onLongItemClick)
        }
    }
}

@Composable
fun NoExpensesFound() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = 96.dp,
                start = 24.dp,
                end = 24.dp
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                )
                .padding(8.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(64.dp),
                imageVector = Icons.Outlined.MoneyOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.no_expenses),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.no_expenses_for_selected_period),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ExpenseListItem(
    item: ExpenseWithCategory,
    onLongItemClick: (Long) -> Unit
) {

    val formatter = SimpleDateFormat.getDateInstance(DateFormat.SHORT)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    onLongItemClick(item.expense.id)
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Color(item.category.color).copy(alpha = 0.15f)
                                )
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = getIconByKey(item.category.iconId),
                                contentDescription = "Expense category icon",
                                modifier = Modifier.size(32.dp),
                                tint = Color(item.category.color)
                            )
                        }
                        Spacer(modifier = Modifier.size(12.dp))
                        Column {
                            Text(
                                text = item.category.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = formatter.format(item.expense.date),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Row {
                        Spacer(modifier = Modifier.width(62.dp))
                        if (item.expense.note != null && item.expense.note != "") {
                            Text(
                                text = item.expense.note!!,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "-${
                    BigDecimal(item.expense.amount.toDouble())
                        .setScale(2, RoundingMode.HALF_UP)
                } Br",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.End
            )
        }
    }
}