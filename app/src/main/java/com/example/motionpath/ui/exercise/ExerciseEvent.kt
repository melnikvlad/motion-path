package com.example.motionpath.ui.exercise

import com.example.motionpath.model.domain.mock_exercise.MockExercise

sealed class ExerciseEvent {
    data class onItemClicked(val item: MockExercise): ExerciseEvent()
    data class onItemRemoveClicked(val item: MockExercise): ExerciseEvent()
    data class onItemMinusClicked(val item: MockExercise): ExerciseEvent()
    data class onItemPlusClicked(val item: MockExercise): ExerciseEvent()
    object onSearchViewClicked: ExerciseEvent()
    object onBackButtonClicked: ExerciseEvent()
}
