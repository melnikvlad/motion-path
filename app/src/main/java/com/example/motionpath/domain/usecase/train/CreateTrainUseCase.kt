package com.example.motionpath.domain.usecase.train

import com.example.motionpath.domain.TrainRepository
import com.example.motionpath.data.model.entity.TrainEntity

class CreateTrainUseCase(private val trainRepository: TrainRepository) {
    suspend operator fun invoke(train: TrainEntity): Long {
        return trainRepository.createTrain(train)
    }
}