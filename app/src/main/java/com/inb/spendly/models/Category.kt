package com.inb.spendly.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var name: String,
    var color: String,
    var iconId: Int,
)