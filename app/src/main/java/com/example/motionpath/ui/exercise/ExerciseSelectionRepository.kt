package com.example.motionpath.ui.exercise

import com.example.motionpath.model.domain.mock_exercise.MockExercise
import com.example.motionpath.model.domain.mock_exercise.MockExerciseListItem
import kotlinx.coroutines.flow.StateFlow

interface ExerciseSelectionRepository {

    fun addExercise(exercise: MockExercise)

    fun removeExercise(exercise: MockExercise)

    fun getSelectedExercises(): StateFlow<List<MockExercise>>

    fun getExerciseCount(exercise: MockExercise): Int

    fun getCategoryExerciseCount(category: MockExercise): Int

    fun clear()
}