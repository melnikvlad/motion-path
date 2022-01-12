package com.example.motionpath.domain

import com.example.motionpath.model.domain.mock_exercise.MockExercise
import kotlinx.coroutines.flow.StateFlow

interface ExerciseSelectionRepository {

    suspend fun addPreSelectedExercises(exercises: List<MockExercise>)

    suspend fun addExercise(exercise: MockExercise)

    suspend fun removeExercise(exercise: MockExercise)

    fun getSelectedExercises(): StateFlow<List<MockExercise>>

    fun getExerciseCount(exercise: MockExercise): Int

    fun getCategoryExerciseCount(category: MockExercise): Int

    fun clear()
}