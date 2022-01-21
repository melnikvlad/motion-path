package com.example.motionpath.data.db

import androidx.room.*
import com.example.motionpath.data.model.entity.ClientEntity
import com.example.motionpath.data.model.entity.relations.ClientWithTrains
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(client: ClientEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(clients: List<ClientEntity>)

    @Query("SELECT * FROM clients")
    fun getAll(): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE name LIKE '%' || :query || '%'")
    fun searchByName(query: String): Flow<List<ClientEntity>>

    @Query("SELECT * FROM clients WHERE id = :clientId")
    fun getById(clientId: Int): ClientEntity?

    @Query("SELECT * FROM clients WHERE category = :category")
    fun getByCategory(category: Int): ClientEntity

    @Transaction
    @Query("SELECT * FROM clients")
    fun getClientsWithTrains(): Flow<List<ClientWithTrains>>

    @Transaction
    @Query("SELECT * FROM clients WHERE id = :clientId")
    fun getClientWithTrains(clientId: Int): ClientWithTrains
}