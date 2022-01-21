package com.example.motionpath.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.motionpath.model.domain.train.Train
import java.util.Date

@Entity(tableName = "trains")
data class TrainEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val clientId: Int,
    val timeStart: Date,
    val timeEnd: Date
)

fun TrainEntity.toDomain(): Train = Train(id ?: -1, clientId, timeStart, timeEnd)