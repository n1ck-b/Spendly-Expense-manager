@file:JvmName("CategoryStateKt")

package com.inb.spendly.presentation.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navController: NavHostController
) {

    val state = categoryViewModel.state.collectAsState()
    val currentState = state.value

    Scaffold(
        floatingActionButton = {
            FloatingActionButtonAdd(
                onClick = {
                    categoryViewModel.processCommand(CategoryCommand.AddCategory)
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { paddingValues ->

        Column(
            modifier = modifier
                .padding(
                    top = paddingValues.calculateTopPadding() + 20.dp,
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 30.dp,
                    end = 30.dp
                )
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            CategoriesScreenHeader()

            DateChips(
                sharedViewModel = sharedViewModel
            )

            when (currentState) {
                is CategoryState.Loaded -> {

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
}

@Composable
fun CategoriesScreenHeader() {
    Text(
        text = stringResource(R.string.category_screen_header),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(top = 20.dp, bottom = 15.dp)
    )
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
        columns = GridCells.Adaptive(minSize = 130.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
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
            .fillMaxWidth()
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(50.dp),
            painter = painterResource(R.drawable.outline_category_search_24),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = stringResource(R.string.no_categories_screen_warning),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CategoriesGridItem(
    item: CategoryWithFilteredExpenses,
    onLongItemClick: (Long) -> Unit
) {
    val withoutCategory = stringResource(R.string.without_category)

    OutlinedCard(
        modifier = Modifier
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    onLongItemClick(item.categoryId)
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(15.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = getIconByKey(item.categoryIconId),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = Color(item.categoryColor)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column {
                Text(
                    text = if (item.categoryName == NO_CATEGORY) withoutCategory
                    else item.categoryName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${
                        BigDecimal(item.expenseAmount?.toDouble() ?: 0.0)
                            .setScale(2, RoundingMode.HALF_UP)
                    } Br"
                )
            }
        }
    }
}