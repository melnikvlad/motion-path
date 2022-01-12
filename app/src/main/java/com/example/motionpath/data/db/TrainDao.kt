package com.example.motionpath.data.db

import androidx.room.*
import com.example.motionpath.data.model.entity.TrainEntity
import com.example.motionpath.data.model.entity.relations.TrainWithClient
import com.example.motionpath.data.model.entity.relations.TrainWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTrain(train: TrainEntity): Long

    @Query("SELECT * FROM trains WHERE timeStart BETWEEN :dateStart AND :dateEnd")
    fun getTrainsForDate(dateStart: Long, dateEnd: Long): Flow<List<TrainWithClient>>

    @Transaction
    @Query("SELECT * FROM trains WHERE id = :trainId")
    fun getTrainWithExercises(trainId: Int): TrainWithExercises

    @Query("DELETE FROM trains WHERE clientId = :clientId")
    fun deleteAllClientTrains(clientId: Int)

    @Delete
    fun deleteTrain(train: TrainEntity)
}