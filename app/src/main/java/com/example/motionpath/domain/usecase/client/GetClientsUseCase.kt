package com.example.motionpath.domain.usecase.client

import com.example.motionpath.data.model.entity.toDomain
import com.example.motionpath.domain.ClientRepository
import com.example.motionpath.ui.create_train.Client
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetClientsUseCase(private val clientRepository: ClientRepository) {
    operator fun invoke(): Flow<List<Client>> {
        return clientRepository.getClients().map { list ->
            list.map { entity -> entity.toDomain() }
        }
    }
}