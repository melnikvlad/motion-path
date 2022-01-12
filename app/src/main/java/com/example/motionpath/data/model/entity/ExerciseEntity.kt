package com.example.motionpath.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.motionpath.model.domain.Exercise
import com.example.motionpath.model.domain.mock_exercise.MockExercise

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val trainId: Int,
    val mockExerciseCategoryId: Int?,
    val mockExerciseId: Int,
    val mockExerciseName: String,
)

fun ExerciseEntity.toDomain(index: Int): Exercise =
    Exercise(
        index = index + 1,
        mockExercise = MockExercise(
            id = mockExerciseId,
            categoryId = mockExerciseCategoryId,
            name = mockExerciseName
        )
    )

fun Exercise.toEntity(): ExerciseEntity =
    trainId?.let {
        ExerciseEntity(
            trainId = it,
            mockExerciseCategoryId = mockExercise.categoryId,
            mockExerciseId = mockExercise.id,
            mockExerciseName = mockExercise.name
        )
    } ?: throw NullPointerException("trainId cann't be null!!!")
