package com.inb.spendly.presentation.screens.statistics

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.inb.spendly.R
import com.inb.spendly.domain.entities.CategoryWithFilteredExpenses
import com.inb.spendly.presentation.screens.categories.CategoryViewModel
import com.inb.spendly.presentation.screens.expenses.DateDropDown
import com.inb.spendly.presentation.screens.SharedViewModel
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun StatisticsScreen(
    paddingValues: PaddingValues,
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel
) {

    val selectedDateRange = sharedViewModel.selectedDateRange

    val categoriesWithExpenses by categoryViewModel.categoriesList.collectAsState()

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
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        StatisticsScreenHeader()
        DateDropDown(
            sharedViewModel = sharedViewModel,
            selectedDateRange = selectedDateRange.collectAsState().value
        )
        StatisticsByCategoriesList(categoriesWithExpenses)
    }
}

@Composable
fun StatisticsScreenHeader() {
    Text(
        text = stringResource(R.string.statistics_screen_header),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(top = 20.dp, bottom = 15.dp)
    )
}

@Composable
fun DonutChart(categoriesWithExpenses: List<CategoryWithFilteredExpenses>) {

    val slices = categoriesWithExpenses.map { category ->
        PieChartData.Slice(
            label = category.categoryName,
            value = category.expenseAmount ?: 0.0f,
            color = Color(category.categoryColor)
        )
    }

    val donutChartData = PieChartData(
        slices = slices,
        plotType = PlotType.Donut
    )

    val donutChartConfig = PieChartConfig(
        sliceLabelTextColor = MaterialTheme.colorScheme.onBackground,
        isSumVisible = true,
        strokeWidth = 120f,
        chartPadding = 20,
        backgroundColor = MaterialTheme.colorScheme.background,
        labelColor = MaterialTheme.colorScheme.onBackground,
        sumUnit = "Br",
        showSliceLabels = true,
        labelVisible = true,
        isAnimationEnable = true,
        labelType = PieChartConfig.LabelType.VALUE
    )

    DonutPieChart(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        pieChartData = donutChartData,
        pieChartConfig = donutChartConfig
    )
}

@Composable
fun NoDataToShow() {
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
            painter = painterResource(R.drawable.baseline_insights_24),
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
fun StatisticsByCategoriesList(categoriesWithExpenses: List<CategoryWithFilteredExpenses>) {

    val sumOfAllExpenses = categoriesWithExpenses.sumOf { it.expenseAmount?.toDouble() ?: 0.0 }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        if(categoriesWithExpenses.isNotEmpty()) {
            item {
                DonutChart(categoriesWithExpenses)
            }
        } else {
            item {
                NoDataToShow()
            }
        }
        items(categoriesWithExpenses) { item ->
            StatisticsByCategoriesListItem(item, sumOfAllExpenses)
        }
        item {
            Spacer(Modifier.height(5.dp))
        }
    }
}

@Composable
fun StatisticsByCategoriesListItem(item: CategoryWithFilteredExpenses, sumOfAllExpenses: Double) {

    val percentage = ((item.expenseAmount ?: 0f) / (sumOfAllExpenses.takeIf { it != 0.0 } ?: 1.0)) * 100

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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(item.categoryIconId),
                    contentDescription = null,
                    tint = Color(item.categoryColor),
                    modifier = Modifier
                        .size(30.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = item.categoryName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "${BigDecimal(item.expenseAmount?.toDouble() ?: 0.0)
                            .setScale(2, RoundingMode.HALF_UP)} Br"
                    )
                }
            }
            Text(
                text = "${BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP)}%",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}