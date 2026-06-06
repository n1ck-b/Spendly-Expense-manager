package com.inb.spendly.presentation.screens.categories

import androidx.compose.ui.graphics.Color
import com.inb.spendly.R
import com.inb.spendly.presentation.ui.theme.DefaultIconColor

data class CategoryState(
    var id: Long = 0,
    var name: String = "",
    var color: Color = DefaultIconColor,
    var iconId: Int = R.drawable.outline_image_24
)