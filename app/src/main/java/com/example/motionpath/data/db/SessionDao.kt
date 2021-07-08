package com.example.motionpath.data.db

import androidx.room.*
import com.example.motionpath.model.entity.SessionEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface SessionDao {

    @Query("SELECT * FROM sessions")
    fun get(): Flow<List<SessionEntity>>

    @Delete
    suspend fun delete(sessionEntity: SessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: SessionEntity)
}