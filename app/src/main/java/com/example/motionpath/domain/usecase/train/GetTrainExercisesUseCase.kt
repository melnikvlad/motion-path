package com.example.motionpath.domain.usecase.train

import com.example.motionpath.domain.TrainRepository
import com.example.motionpath.model.domain.Exercise
import com.example.motionpath.model.entity.relations.TrainWithExercises
import com.example.motionpath.model.entity.toDomain

class GetTrainExercisesUseCase(private val trainRepository: TrainRepository) {
    suspend operator fun invoke(trainId: Int): List<Exercise> {
        return trainRepository.getTrainWithExercises(trainId).exercises.map { it.toDomain() }
    }
}