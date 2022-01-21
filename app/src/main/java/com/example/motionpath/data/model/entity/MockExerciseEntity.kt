package com.example.motionpath.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.motionpath.model.domain.mock_exercise.MockExercise

@Entity(tableName = "mock_exercises")
data class MockExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val mockExerciseid: Int? = null,
    val categoryId: Int? = null,
    val categoryName: String? = null,
    val exerciseName: String,
    val muscleName: String? = null
)

fun MockExerciseEntity.toDomain() = MockExercise(
    id = mockExerciseid ?: -1,
    categoryId = categoryId,
    name = exerciseName,
    categoryName = categoryName,
    muscleName = muscleName
)

fun MockExercise.toEntity() = MockExerciseEntity(
    mockExerciseid = id,
    categoryId = categoryId,
    exerciseName = name,
    muscleName = muscleName,
    categoryName = categoryName
)
