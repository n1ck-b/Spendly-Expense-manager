package com.inb.spendly.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryDbModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var color: Int,
    var iconId: String,
)