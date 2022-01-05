package com.example.motionpath.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.motionpath.model.domain.Client
import com.example.motionpath.model.domain.getCategoryType

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String,
    var category: Int = 0,
    var description: String = "",
    var goal: String = "",
)

fun Client.toEntity(): ClientEntity = ClientEntity(
    id = id,
    name = name,
    description = description,
    goal = goal
)

fun ClientEntity.toDomain(): Client = Client(
    id = id ?: -1,
    name = name,
    categoryType = category.getCategoryType(),
    description = description,
    goal = goal
)