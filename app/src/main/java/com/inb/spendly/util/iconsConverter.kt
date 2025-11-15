package com.inb.spendly.util

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector

fun getNameOfIcon(icon: ImageVector): String {
    return icon.name.split(".")[1]
}

fun getIconByName(name: String): ImageVector {
    // получаем класс иконки
    val cl = Class.forName("androidx.compose.material.icons.filled.${name}Kt")
    // получаем ее геттер
    val method = cl.declaredMethods.first()
    return method.invoke(null, Icons.Filled) as ImageVector
}