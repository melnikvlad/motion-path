package com.example.motionpath.domain.usecase.client

import com.example.motionpath.domain.ClientRepository
import com.example.motionpath.data.model.entity.relations.ClientWithTrains
import kotlinx.coroutines.flow.Flow

class GetClientsWithTrainsUseCase(private val clientRepository: ClientRepository) {
    suspend operator fun invoke(): Flow<List<ClientWithTrains>> {
        return clientRepository.getClientsWithTrains()
    }
}