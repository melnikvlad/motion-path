package com.example.motionpath.model.domain

sealed class MockExerciseListItemViewType(code: Int): BaseListItemViewType(code) {
    object MockExerciseTitle: MockExerciseListItemViewType(0)
    object MockExerciseItem: MockExerciseListItemViewType(1)
    object MockExerciseSelectedItem: MockExerciseListItemViewType(2)
}

open class BaseListItemViewType(val code: Int)
