package com.example.motionpath.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.motionpath.model.domain.mock_exercise.MockExercise

@Entity(tableName = "mock_exercises")
data class MockExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val parentId: Int? = null,
    val exerciseName: String,
    val muscleName: String? = null
)

fun MockExerciseEntity.toDomain() = MockExercise(
    id = id ?: -1,
    categoryId = parentId,
    name = exerciseName,
    muscleName = muscleName
)

fun MockExercise.toEntity() = MockExerciseEntity(
    id = id,
    parentId = categoryId,
    exerciseName = name,
    muscleName = muscleName
)
