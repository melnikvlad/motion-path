package com.example.motionpath.ui.exercise

import com.example.motionpath.model.domain.mock_exercise.MockExercise

class ExerciseSelectionManager {
    private val map: Map<Int, ArrayList<MockExercise>> = LinkedHashMap()

    fun toggle(exercise: MockExercise) {
        exercise.categoryId?.let { categoryId ->
            val isSelected = map[categoryId]?.firstOrNull { it.id == exercise.id } != null
            when {
                isSelected -> map[categoryId]?.remove(exercise)
                else -> map[categoryId]?.add(exercise)
            }
        }

    }
}