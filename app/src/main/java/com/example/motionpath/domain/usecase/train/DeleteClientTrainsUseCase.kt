package com.example.motionpath.domain.usecase.train

import com.example.motionpath.domain.TrainRepository

class DeleteClientTrainsUseCase(private val trainRepository: TrainRepository) {
    suspend operator fun invoke(clientId: Int) {
        trainRepository.deleteAllClientTrains(clientId)
    }
}