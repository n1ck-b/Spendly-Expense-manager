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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.inb.spendly.R
import com.inb.spendly.ui.components.DropDownMenu

@Composable
fun StatisticsScreen(paddingValues: PaddingValues) {
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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StatisticsScreenHeader()
        Spacer(Modifier.height(10.dp))
        DropDownMenu(stringArrayResource(R.array.time_periods).toList())
        StatisticsByCategoriesList()
    }
}

@Composable
fun StatisticsScreenHeader() {
    Text(
        text = stringResource(R.string.statistics_screen_header),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(top = 20.dp)
    )
}

@Composable
fun DonutChart() {
    val donutChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice("Family", 450f, Color.Blue),
            PieChartData.Slice("Food", 700f, Color.Cyan)
        ),
        plotType = PlotType.Donut
    )

    val donutChartConfig = PieChartConfig(
        sliceLabelTextColor = Color.Black,
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
            .padding(top = 10.dp, bottom = 10.dp),
        pieChartData = donutChartData,
        pieChartConfig = donutChartConfig
    )
}

@Composable
fun StatisticsByCategoriesList() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        item {
            DonutChart()
        }
        items(10) {
            StatisticsByCategoriesListItem()
        }
    }
}

@Composable
fun StatisticsByCategoriesListItem() {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(horizontal = 20.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = null,
                Modifier.size(30.dp),
                tint = Color.Blue
            )
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = "Food",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "750 Br"
                )
            }
        }
        Text(
            text = "56%",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StatisticsScreenPreview() {
    StatisticsScreen(PaddingValues(30.dp))
}