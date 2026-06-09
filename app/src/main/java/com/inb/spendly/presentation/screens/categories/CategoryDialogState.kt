package com.inb.spendly.presentation.screens.categories

import androidx.compose.ui.graphics.Color
import com.inb.spendly.presentation.ui.theme.CategoryIcons
import com.inb.spendly.presentation.ui.theme.DefaultIconColor

sealed interface CategoryDialogState {

    data object Closed: CategoryDialogState

    data class AddingCategory(
        val editing: Boolean = false,
        val id: Long = 0,
        val name: String = "",
        val color: Color = DefaultIconColor,
        val iconId: String = CategoryIcons.DEFAULT_ICON_KEY
    ): CategoryDialogState

    data class SelectingAction(val categoryId: Long): CategoryDialogState

}