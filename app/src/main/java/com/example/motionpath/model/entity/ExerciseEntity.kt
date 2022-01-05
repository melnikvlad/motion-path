package com.example.motionpath.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.motionpath.model.domain.Exercise

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val trainId: Int,
)

fun ExerciseEntity.toDomain(): Exercise {
    return Exercise(id = id, trainId = trainId)
}

fun Exercise.toEntity(): ExerciseEntity {
    return ExerciseEntity(id, trainId)
}
