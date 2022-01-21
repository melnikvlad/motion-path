package com.example.motionpath.data.exercise

import com.example.motionpath.data.db.ExerciseDao
import com.example.motionpath.data.model.entity.ExerciseEntity
import com.example.motionpath.domain.usecase.ExerciseRepository

class ExerciseRepositoryImpl(private val exerciseDao: ExerciseDao) : ExerciseRepository {

    override suspend fun insertExercises(exercises: List<ExerciseEntity>) {
        exerciseDao.insertExercises(exercises)
    }
}