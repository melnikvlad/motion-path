package com.example.motionpath.domain.usecase

import com.example.motionpath.data.model.entity.ExerciseEntity

interface ExerciseRepository {
    suspend fun insertExercises(exercises: List<ExerciseEntity>)
}