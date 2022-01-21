package com.example.motionpath.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.motionpath.data.model.entity.MockExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MockExerciseDao {

    @Insert
    suspend fun insert(mockExercises: List<MockExerciseEntity>)

    @Query("SELECT * FROM mock_exercises WHERE categoryId is NULL")
    fun get(): Flow<List<MockExerciseEntity>>

    @Query("SELECT * FROM mock_exercises WHERE categoryId = :categoryId")
    fun get(categoryId: Int): Flow<List<MockExerciseEntity>>

    @Query("SELECT * FROM mock_exercises WHERE exerciseName LIKE '%' || :query || '%' OR muscleName LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<MockExerciseEntity>>
}