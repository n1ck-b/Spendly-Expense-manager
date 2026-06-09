package com.inb.spendly.presentation.screens.categories

import androidx.compose.ui.graphics.Color

sealed interface CategoryCommand {

    data class InputName(val name: String): CategoryCommand

    data class InputColor(val color: Color): CategoryCommand

    data class InputIconId(val iconId: String): CategoryCommand

    data class SelectAction(val categoryId: Long): CategoryCommand

    data object EditCategory: CategoryCommand

    data object SaveCategory: CategoryCommand

    data class DeleteCategory(val categoryId: Long): CategoryCommand

    data object ReturnToList: CategoryCommand

    data object AddCategory: CategoryCommand

    data object SaveEditedCategory: CategoryCommand

}