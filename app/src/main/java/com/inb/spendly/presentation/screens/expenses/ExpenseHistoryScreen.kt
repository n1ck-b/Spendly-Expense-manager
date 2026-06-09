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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.inb.spendly.R
import com.inb.spendly.domain.entities.ExpenseWithCategory
import com.inb.spendly.presentation.components.ActionDialog
import com.inb.spendly.presentation.components.BottomNavBarItem
import com.inb.spendly.presentation.components.BottomNavigationBar
import com.inb.spendly.presentation.components.DateChips
import com.inb.spendly.presentation.components.FloatingActionButtonAdd
import com.inb.spendly.presentation.screens.SharedViewModel
import com.inb.spendly.presentation.ui.theme.CategoryIcons.getIconByKey
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat

@Composable
fun ExpenseHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: ExpenseViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navController: NavHostController,
    bottomNavBarItems: List<BottomNavBarItem>
) {

    val state = viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButtonAdd(
                onClick = {
                    viewModel.processCommand(ExpenseCommand.AddExpense)
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController, bottomNavBarItems)
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
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
            DateChips(
                sharedViewModel = sharedViewModel
            )

            when (val currentState = state.value) {
                is ExpenseState.Loaded -> {

                    ExpenseAmountTile(
                        currentState.expenses
                            .sumOf { it.expense.amount.toDouble() }.toFloat()
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
                    text = "${
                        BigDecimal(sumForSelectedPeriod.toDouble())
                            .setScale(2, RoundingMode.HALF_UP)
                    } Br",
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
        if (expensesWithCategory.isEmpty()) {
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = getIconByKey(item.category.iconId),
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
                        if (item.expense.note != null && item.expense.note != "") {
                            Text(
                                text = item.expense.note!!
                            )
                        }
                    }
                }
            }
            Text(
                text = "-${
                    BigDecimal(item.expense.amount.toDouble())
                        .setScale(2, RoundingMode.HALF_UP)
                } Br"
            )
        }
    }
}