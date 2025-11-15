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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inb.spendly.R
import com.inb.spendly.ui.components.DropDownMenu

@Composable
fun CategoriesScreen(paddingValues: PaddingValues) {
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
//        DropDownMenu(stringArrayResource(R.array.time_periods).toList())
        DateDropDown()
        CategoriesGrid()
    }
}

@Composable
fun CategoriesScreenHeader() {
    Text(
        text = stringResource(R.string.category_screen_header),
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(top = 20.dp)
    )
}

@Composable
fun CategoriesGrid() {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 130.dp),
        contentPadding = PaddingValues(top = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(20) {
            CategoriesGridItem()
        }
    }
}

@Composable
fun CategoriesGridItem() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(Color.LightGray)
            .padding(horizontal = 20.dp, vertical = 15.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Column(

        ) {
            Text(
                text = "Family",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "150 Br"
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CategoriesScreenPreview() {
    CategoriesScreen(PaddingValues(20.dp))
}