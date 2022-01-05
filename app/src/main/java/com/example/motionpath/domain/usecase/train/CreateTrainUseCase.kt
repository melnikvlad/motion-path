package com.example.motionpath.domain.usecase.train

import com.example.motionpath.domain.TrainRepository
import com.example.motionpath.model.entity.TrainEntity

class CreateTrainUseCase(private val trainRepository: TrainRepository) {
    suspend operator fun invoke(train: TrainEntity) {
        trainRepository.createTrain(train)
    }
}