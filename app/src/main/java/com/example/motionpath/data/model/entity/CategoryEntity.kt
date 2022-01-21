package com.example.motionpath.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val isDefault: Boolean = true
)