package com.example.motionpath.domain.usecase.exercise

import com.example.motionpath.data.model.entity.ExerciseEntity
import com.example.motionpath.domain.usecase.ExerciseRepository

class InsertExercisesUseCase(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(exercises: List<ExerciseEntity>) {
        repository.insertExercises(exercises)
    }
}