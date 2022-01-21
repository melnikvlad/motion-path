package com.example.motionpath.ui.exercise

sealed class ExerciseAction {
    data class ShowSnackBar(val error: Throwable): ExerciseAction()
}
