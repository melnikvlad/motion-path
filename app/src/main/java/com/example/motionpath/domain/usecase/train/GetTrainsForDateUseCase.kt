package com.example.motionpath.domain.usecase.train

import com.example.motionpath.domain.TrainRepository
import com.example.motionpath.data.model.entity.relations.TrainWithClient
import kotlinx.coroutines.flow.Flow

class GetTrainsForDateUseCase(private val trainRepository: TrainRepository) {
    // TODO: to domain train model
    operator fun invoke(dateStart: Long, dateEnd: Long): Flow<List<TrainWithClient>> {
        return trainRepository.getTrainsForDate(dateStart, dateEnd)
    }
}