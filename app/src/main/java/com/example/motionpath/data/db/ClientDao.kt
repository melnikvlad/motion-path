package com.example.motionpath.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.motionpath.model.entity.ClientEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ClientDao {

    @Query("SELECT * FROM clients")
    fun get(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE date == :date")
    fun get(date: Date): Flow<List<ClientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(client: ClientEntity)
}