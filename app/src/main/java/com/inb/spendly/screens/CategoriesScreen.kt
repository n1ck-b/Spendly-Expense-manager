package com.inb.spendly.screens

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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inb.spendly.R
import com.inb.spendly.models.relations.CategoryWithFilteredExpenses
import com.inb.spendly.viewmodels.CategoryViewModel
import com.inb.spendly.viewmodels.SharedViewModel
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun CategoriesScreen(
    paddingValues: PaddingValues,
    categoryViewModel: CategoryViewModel,
    sharedViewModel: SharedViewModel
) {

    val selectedDateRange = sharedViewModel.selectedDateRange

    val categoriesWithExpenses by categoryViewModel.categoriesList.collectAsState()

    val showActionDialog = remember { mutableStateOf(false) }

    val withoutCategoryString = stringResource(R.string.without_category)

    Column(
        modifier = Modifier
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
        DateDropDown(
            selectedDateRange = selectedDateRange.collectAsState().value,
            sharedViewModel = sharedViewModel
        )
        CategoriesGrid(
            categoriesWithExpenses,
            onLongItemClick = {
                showActionDialog.value = true
                categoryViewModel.updateSelectedCategoryId(it)
            }
        )
        ActionDialog(
            showDialog = showActionDialog.value,
            onDismissRequest = {
                showActionDialog.value = false
            },
            onEditButtonClicked = {
                sharedViewModel.updateShowCategoryDialog(true)
                categoryViewModel.updateState()
                showActionDialog.value = false
            },
            onDeleteButtonClicked = {
                categoryViewModel.deleteCategory(withoutCategoryString)
                showActionDialog.value = false
            }
        )
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

    if(categoriesWithExpenses.isEmpty()) {
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
                painter = painterResource(item.categoryIconId),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = Color(item.categoryColor)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column {
                Text(
                    text = item.categoryName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${BigDecimal(item.expenseAmount?.toDouble() ?: 0.0)
                        .setScale(2, RoundingMode.HALF_UP)} Br"
                )
            }
        }
    }
}