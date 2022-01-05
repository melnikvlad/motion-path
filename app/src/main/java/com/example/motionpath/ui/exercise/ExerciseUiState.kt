package com.example.motionpath.ui.exercise

import com.example.motionpath.model.domain.MockExercise

data class ExerciseUiState(
    val categories: List<MockExercise> = emptyList(),
    val exercise: List<MockExercise> = emptyList(),
    val depth: SelectionDepth = SelectionDepth.CATEGORY,
    val status: Status
)

sealed class Status {
    object Loading : Status()
    object Empty: Status()
    object Data: Status()
    data class Error(val error: Throwable): Status()
}

enum class SelectionDepth {
    CATEGORY, EXERCISE
}