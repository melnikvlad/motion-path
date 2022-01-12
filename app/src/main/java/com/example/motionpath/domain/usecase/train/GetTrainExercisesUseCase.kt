package com.example.motionpath.domain.usecase.train

import com.example.motionpath.data.model.entity.toDomain
import com.example.motionpath.domain.TrainRepository
import com.example.motionpath.model.domain.Exercise

class GetTrainExercisesUseCase(private val trainRepository: TrainRepository) {
    suspend operator fun invoke(trainId: Int): List<Exercise> {
        return trainRepository.getTrainWithExercises(trainId).exercises
            .mapIndexed { index, entity ->
                entity.toDomain(index)
        }
    }
}