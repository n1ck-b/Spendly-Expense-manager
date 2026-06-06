package com.inb.spendly.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var color: Int,
    var iconId: Int,
)