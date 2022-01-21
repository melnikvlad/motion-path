package com.example.motionpath.data.model.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.motionpath.model.domain.Exercise
import com.example.motionpath.model.domain.mock_exercise.MockExercise

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val trainId: Int,
    @Embedded
    val mockExercise: MockExerciseEntity
)

fun ExerciseEntity.toDomain(index: Int): Exercise =
    Exercise(
        index = index + 1,
        mockExercise = mockExercise.toDomain()
    )

fun Exercise.toEntity(): ExerciseEntity =
    trainId?.let {
        ExerciseEntity(
            trainId = it,
            mockExercise = mockExercise.toEntity()
        )
    } ?: throw NullPointerException("trainId cann't be null!!!")
