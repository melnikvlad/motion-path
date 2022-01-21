package com.example.motionpath.domain

import com.example.motionpath.data.model.entity.TrainEntity
import com.example.motionpath.data.model.entity.relations.TrainWithClient
import com.example.motionpath.data.model.entity.relations.TrainWithExercises
import kotlinx.coroutines.flow.Flow

interface TrainRepository {
    suspend fun createTrain(train: TrainEntity): Long

    fun getTrainsForDate(dateStart: Long, dateEnd: Long): Flow<List<TrainWithClient>>

    suspend fun getTrainWithExercises(trainId: Int): TrainWithExercises

    fun getClientTrainsWithExercises(clientId: Int, currentTrainTime: Long): Flow<List<TrainWithExercises>>

    suspend fun deleteTrain(train: TrainEntity)

    suspend fun deleteAllClientTrains(clientId: Int)
}