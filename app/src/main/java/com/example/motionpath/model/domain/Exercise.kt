package com.example.motionpath.model.domain

import com.example.motionpath.model.domain.mock_exercise.MockExercise

data class Exercise(
    val mockExercise: MockExercise,
    val units: List<ExerciseUnit> = ExerciseUnit.values().toList(),
    val index: Int = -1,
    val trainId: Int? = null
)

enum class ExerciseUnit {
    WEIGHT, COUNT, ATTEMPTS
}
