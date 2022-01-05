package com.example.motionpath.domain.usecase.client

import com.example.motionpath.domain.ClientRepository
import com.example.motionpath.model.domain.Client
import com.example.motionpath.model.entity.ClientEntity
import com.example.motionpath.model.entity.toDomain

class GetClientUseCase(private val clientRepository: ClientRepository) {
    suspend operator fun invoke(clientId: Int): Client? {
        return clientRepository.getClientById(clientId)?.toDomain()
    }
}