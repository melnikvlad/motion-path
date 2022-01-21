package com.example.motionpath.data.train

import com.example.motionpath.data.db.TrainDao
import com.example.motionpath.domain.TrainRepository
import com.example.motionpath.data.model.entity.TrainEntity
import com.example.motionpath.data.model.entity.relations.TrainWithClient
import com.example.motionpath.data.model.entity.relations.TrainWithExercises
import kotlinx.coroutines.flow.Flow

class TrainRepositoryImpl(private val trainDao: TrainDao): TrainRepository {
    override suspend fun createTrain(train: TrainEntity): Long {
        return trainDao.createTrain(train)
    }

    override fun getTrainsForDate(dateStart: Long, dateEnd: Long): Flow<List<TrainWithClient>> {
        return trainDao.getTrainsForDate(dateStart, dateEnd)
    }

    override suspend fun getTrainWithExercises(trainId: Int): TrainWithExercises {
        return trainDao.getTrainWithExercises(trainId)
    }

    override fun getClientTrainsWithExercises(clientId: Int, currentTrainTime: Long): Flow<List<TrainWithExercises>> {
        return trainDao.getClientTrainsWithExercises(clientId, currentTrainTime)
    }

    override suspend fun deleteTrain(train: TrainEntity) {
        trainDao.deleteTrain(train)
    }

    override suspend fun deleteAllClientTrains(clientId: Int) {
        trainDao.deleteAllClientTrains(clientId)
    }
}