package com.example.motionpath.data.client

import com.example.motionpath.data.db.ClientDao
import com.example.motionpath.data.model.entity.ClientEntity
import com.example.motionpath.domain.ClientRepository
import com.example.motionpath.data.model.entity.relations.ClientWithTrains
import kotlinx.coroutines.flow.Flow

class ClientRepositoryImpl(private val clientDao: ClientDao): ClientRepository {

    override fun getClients(): Flow<List<ClientEntity>> = clientDao.getAll()

    override suspend fun getClientById(clientId: Int) = clientDao.getById(clientId)

    override suspend fun createClient(clientEntity: ClientEntity): Long = clientDao.insert(clientEntity)

    override suspend fun getClientsWithTrains(): Flow<List<ClientWithTrains>> = clientDao.getClientsWithTrains()

    override fun searchClients(query: String): Flow<List<ClientEntity>> {
        return clientDao.searchByName(query)
    }
}