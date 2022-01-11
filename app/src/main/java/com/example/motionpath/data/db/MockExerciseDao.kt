package com.example.motionpath.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.motionpath.model.entity.MockExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MockExerciseDao {

    @Insert
    suspend fun insert(mockExercises: List<MockExerciseEntity>)

    @Query("SELECT * FROM mock_exercises WHERE parentId is NULL")
    fun get(): Flow<List<MockExerciseEntity>>

    @Query("SELECT * FROM mock_exercises WHERE parentId = :parentId")
    fun get(parentId: Int): Flow<List<MockExerciseEntity>>

    @Query("SELECT * FROM mock_exercises WHERE exerciseName LIKE '%' || :query || '%' OR muscleName LIKE '%' || :query || '%'")
    fun search(query: String): Flow<List<MockExerciseEntity>>
}