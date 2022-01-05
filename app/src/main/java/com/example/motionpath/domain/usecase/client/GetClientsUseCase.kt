package com.example.motionpath.domain.usecase.client

import com.example.motionpath.domain.ClientRepository
import com.example.motionpath.model.domain.Client
import com.example.motionpath.model.entity.ClientEntity
import com.example.motionpath.model.entity.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map

class GetClientsUseCase(private val clientRepository: ClientRepository) {
    operator fun invoke(): Flow<List<Client>> {
        return clientRepository.getClients().map { list ->
            list.map { entity -> entity.toDomain() }
        }
    }
}