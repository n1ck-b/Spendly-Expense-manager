package com.inb.spendly.presentation.screens.statistics

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.inb.spendly.R
import com.inb.spendly.domain.Constants.Companion.NO_CATEGORY
import com.inb.spendly.domain.entities.CategoryWithFilteredExpenses
import com.inb.spendly.presentation.components.BottomNavigationBar
import com.inb.spendly.presentation.components.DateChips
import com.inb.spendly.presentation.screens.SharedViewModel
import com.inb.spendly.presentation.ui.theme.CategoryIcons.getIconByKey
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.Pie
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun StatisticsScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel,
    statisticsViewModel: StatisticsViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val state = statisticsViewModel.state.collectAsState()
    val currentState = state.value

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Scaffold { paddingValues ->
            Column(
                modifier = modifier
                    .padding(
                        top = paddingValues.calculateTopPadding() + 8.dp,
                    )
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatisticsScreenHeader()
                Spacer(Modifier.size(4.dp))
                DateChips(
                    sharedViewModel = sharedViewModel
                )

                when (currentState) {
                    is StatisticsState.Loaded -> {

                        val sortedCategoriesWithExpenses =
                            currentState.categoriesWithExpenses
                                .filter { it.expenseAmount != 0f && it.expenseAmount != null }
                                .sortedByDescending { it.expenseAmount }

                        if (sortedCategoriesWithExpenses.isNotEmpty()) {
                            ChartPile(categoriesWithExpenses = sortedCategoriesWithExpenses)
                        }

                        StatisticsByCategoriesList(sortedCategoriesWithExpenses)
                    }

                    StatisticsState.Loading -> {
                        // TODO
                    }
                }
            }
        }
        BottomNavigationBar(navController)
    }
}

@Composable
fun StatisticsScreenHeader() {
    Text(
        text = stringResource(R.string.statistics_screen_header),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(
                start = 24.dp,
                end = 24.dp
            )
    )
}

@Composable
fun DonutChartYCharts(
    categoriesWithExpenses: List<CategoryWithFilteredExpenses>
) {

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
        sliceLabelTextColor = MaterialTheme.colorScheme.onPrimary,
        sliceLabelTextSize = 14.sp,
        isSumVisible = true,
        strokeWidth = 60f,
        chartPadding = 24,
        backgroundColor = MaterialTheme.colorScheme.background,
        labelColor = MaterialTheme.colorScheme.onPrimary,
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
fun ChartPile(
    modifier: Modifier = Modifier,
    categoriesWithExpenses: List<CategoryWithFilteredExpenses>
) {
    val sumOfAllExpenses = categoriesWithExpenses.sumOf { it.expenseAmount?.toDouble() ?: 0.0 }

    Row(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                .weight(0.4f),
            contentAlignment = Alignment.Center
        ) {
            DonutChart(
                categoriesWithExpenses = categoriesWithExpenses
            )
        }

        Column(
            modifier = Modifier
                .padding(end = 16.dp, top = 16.dp, bottom = 16.dp)
                .weight(0.6f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            categoriesWithExpenses.forEach {
                ChartLegendItem(
                    category = it,
                    sumOfAllExpenses = sumOfAllExpenses
                )
            }
        }
    }
}

@Composable
fun ChartLegendItem(
    category: CategoryWithFilteredExpenses,
    sumOfAllExpenses: Double
) {
    val percentage = ((category.expenseAmount ?: 0f) /
            (sumOfAllExpenses.takeIf { it != 0.0 } ?: 1.0)) * 100

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(category.categoryColor))
            )
            Text(
                modifier = Modifier.padding(end = 4.dp),
                text = category.categoryName,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start
            )
        }
        Text(
            text = "${BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP)}%",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    categoriesWithExpenses: List<CategoryWithFilteredExpenses>
) {
    val data = remember {
        mutableStateOf(
            categoriesWithExpenses.map { category ->
                Pie(
                    label = category.categoryName,
                    data = (category.expenseAmount ?: 0f).toDouble(),
                    color = Color(category.categoryColor),
                )
            }
        )
    }

    PieChart(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        data = data.value,
        style = Pie.Style.Stroke(width = 16.dp),
        labelHelperProperties = LabelHelperProperties(enabled = false),
        scaleAnimExitSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        onPieClick = {
            val pieIndex = data.value.indexOf(it)
            data.value = data.value.mapIndexed { mapIndex, pie ->
                pie.copy(selected = pieIndex == mapIndex)
            }
        },
        selectedScale = 1.1f
    )
}

@Composable
fun NoDataToShow() {
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
                imageVector = Icons.Outlined.Insights,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.no_data_to_show),
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
fun StatisticsByCategoriesList(
    categoriesWithExpenses: List<CategoryWithFilteredExpenses>
) {

    val sumOfAllExpenses = categoriesWithExpenses.sumOf { it.expenseAmount?.toDouble() ?: 0.0 }

    if (categoriesWithExpenses.isEmpty()) {
        NoDataToShow()
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
        items(categoriesWithExpenses) { item ->
            StatisticsByCategoriesListItem(item, sumOfAllExpenses)
        }
    }
}

@Composable
fun StatisticsByCategoriesListItem(
    item: CategoryWithFilteredExpenses,
    sumOfAllExpenses: Double
) {

    val percentage = ((item.expenseAmount ?: 0f) /
            (sumOfAllExpenses.takeIf { it != 0.0 } ?: 1.0)) * 100

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
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
                        tint = Color(item.categoryColor),
                        modifier = Modifier.size(32.dp)
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
                        "${
                            BigDecimal(item.expenseAmount?.toDouble() ?: 0.0)
                                .setScale(2, RoundingMode.HALF_UP)
                        } Br",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "${BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP)}%",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.End
            )
        }
    }
}