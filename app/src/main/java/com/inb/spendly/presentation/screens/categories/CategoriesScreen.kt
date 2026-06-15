@file:JvmName("CategoryStateKt")

package com.inb.spendly.presentation.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.MoneyOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
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
import com.inb.spendly.data.models.Currencies
import com.inb.spendly.domain.Constants.Companion.NO_CATEGORY
import com.inb.spendly.domain.entities.CategoryWithFilteredExpenses
import com.inb.spendly.presentation.components.ActionDialog
import com.inb.spendly.presentation.components.BottomNavigationBar
import com.inb.spendly.presentation.components.DateChips
import com.inb.spendly.presentation.components.FloatingActionButtonAdd
import com.inb.spendly.presentation.screens.SharedViewModel
import com.inb.spendly.presentation.ui.theme.CategoryIcons.getIconByKey
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.collections.maxBy

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {

    val state = categoryViewModel.state.collectAsState()
    val currentState = state.value

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButtonAdd(
                    onClick = {
                        categoryViewModel.processCommand(CategoryCommand.AddCategory)
                    }
                )
            }
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
                CategoriesScreenHeader()

                Spacer(Modifier.size(4.dp))

                DateChips(sharedViewModel = sharedViewModel)

                when (currentState) {
                    is CategoryState.Loaded -> {

                        CategoriesStatisticsTile(
                            sumForSelectedPeriod = currentState.categories
                                .sumOf { it.expenseAmount?.toDouble() ?: 0.0 }.toFloat(),
                            topCategoryName = currentState.categories.maxByOrNull {
                                it.expenseAmount ?: 0.0f
                            }?.categoryName,
                            topCategorySum = currentState.categories.maxByOrNull {
                                it.expenseAmount ?: 0.0f
                            }?.expenseAmount,
                        )

                        CategoriesGrid(
                            categoriesWithExpenses = currentState.categories,
                            onLongItemClick = {
                                categoryViewModel.processCommand(CategoryCommand.SelectAction(it))
                            }
                        )

                        when (currentState.dialogState) {
                            is CategoryDialogState.AddingCategory -> {
                                AddingCategoryDialog(
                                    onDismissRequest = {
                                        categoryViewModel.processCommand(CategoryCommand.ReturnToList)
                                    },
                                    onSaveButtonClicked = {
                                        if (currentState.dialogState.editing) {
                                            categoryViewModel.processCommand(
                                                CategoryCommand.SaveEditedCategory
                                            )
                                        } else {
                                            categoryViewModel.processCommand(
                                                CategoryCommand.SaveCategory
                                            )
                                        }
                                    }
                                )
                            }

                            CategoryDialogState.Closed -> {}

                            is CategoryDialogState.SelectingAction -> {
                                ActionDialog(
                                    onDismissRequest = {
                                        categoryViewModel.processCommand(CategoryCommand.ReturnToList)
                                    },
                                    onEditButtonClicked = {
                                        categoryViewModel.processCommand(CategoryCommand.EditCategory)
                                    },
                                    onDeleteButtonClicked = {
                                        categoryViewModel.processCommand(
                                            CategoryCommand.DeleteCategory(
                                                currentState.dialogState.categoryId
                                            )
                                        )
                                    }
                                )
                            }
                        }

                    }

                    CategoryState.Loading -> {
                        // TODO
                    }
                }
            }
        }
        BottomNavigationBar(navController)
    }
}

@Composable
fun CategoriesScreenHeader() {
    Text(
        text = stringResource(R.string.category_screen_header),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp)
    )
}

@Composable
fun CategoriesStatisticsTile(
    sumForSelectedPeriod: Float,
    topCategoryName: String?,
    topCategorySum: Float?,
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${
                        BigDecimal(sumForSelectedPeriod.toDouble()).setScale(
                            2,
                            RoundingMode.HALF_UP
                        )
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (topCategoryName != null && topCategorySum != null) {
                    Text(
                        text = topCategoryName,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        maxLines = 1
                    )
                    Text(
                        text = "${stringResource(R.string.top_category_amount_header)} (${
                            BigDecimal(topCategorySum.toDouble()).setScale(
                                2,
                                RoundingMode.HALF_UP
                            )
                        } Br)",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 2
                    )
                } else {
                    Text(
                        text = "—",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = stringResource(R.string.top_category_amount_header),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@Composable
fun CategoriesGrid(
    categoriesWithExpenses: List<CategoryWithFilteredExpenses>,
    onLongItemClick: (Long) -> Unit
) {

    if (categoriesWithExpenses.isEmpty()) {
        NoCategoriesFound()
    }

    LazyVerticalGrid(
        contentPadding = PaddingValues(
            top = 20.dp,
            bottom = 20.dp + 120.dp,
            start = 24.dp,
            end = 24.dp
        ),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categoriesWithExpenses) { categoryWithExpenses ->
            CategoriesGridItem(categoryWithExpenses, onLongItemClick)
        }
    }
}

@Composable
fun NoCategoriesFound() {
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
                imageVector = Icons.Outlined.Category,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.no_categories),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.no_categories_description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CategoriesGridItem(
    item: CategoryWithFilteredExpenses,
    onLongItemClick: (Long) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    onLongItemClick(item.categoryId)
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Color(item.categoryColor).copy(alpha = 0.15f)
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = getIconByKey(item.categoryIconId),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color(item.categoryColor)
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            Column {
                Text(
                    text = if (item.categoryName == NO_CATEGORY) stringResource(R.string.without_category)
                    else item.categoryName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "${
                        BigDecimal(item.expenseAmount?.toDouble() ?: 0.0)
                            .setScale(2, RoundingMode.HALF_UP)
                    } Br",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}