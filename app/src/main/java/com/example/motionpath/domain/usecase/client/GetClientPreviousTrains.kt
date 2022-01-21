package com.example.motionpath.domain.usecase.client

import com.example.motionpath.data.model.entity.relations.toDomain
import com.example.motionpath.domain.TrainRepository
import com.example.motionpath.model.domain.train.Train
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetClientPreviousTrains(private val repository: TrainRepository) {
    suspend operator fun invoke(clientId: Int, currentTrainTime: Long): Flow<List<Train>> {
        return repository.getClientTrainsWithExercises(clientId, currentTrainTime)
            .map { list ->
                list.map { it.toDomain() }
            }
    }
}