package com.example.motionpath.ui.exercise

import com.example.motionpath.model.domain.MockExercise

sealed class ExerciseEvent {
    data class onItemClicked(val item: MockExercise): ExerciseEvent()
}
