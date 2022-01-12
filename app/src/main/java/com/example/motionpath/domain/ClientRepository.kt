package com.example.motionpath.domain

import com.example.motionpath.data.model.entity.ClientEntity
import com.example.motionpath.data.model.entity.relations.ClientWithTrains
import kotlinx.coroutines.flow.Flow

interface ClientRepository {
    fun getClients(): Flow<List<ClientEntity>>

    suspend fun getClientById(clientId: Int): ClientEntity?

    suspend fun createClient(clientEntity: ClientEntity): Long

    suspend fun getClientsWithTrains(): Flow<List<ClientWithTrains>>
}