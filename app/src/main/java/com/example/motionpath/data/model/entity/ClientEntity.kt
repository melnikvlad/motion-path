package com.example.motionpath.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.motionpath.model.domain.client.Client
import com.example.motionpath.model.domain.client_category.getCategoryType

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