package com.example.motionpath.domain.usecase.client

import com.example.motionpath.data.model.entity.toDomain
import com.example.motionpath.domain.ClientRepository
import com.example.motionpath.ui.create_train.Client

class GetClientUseCase(private val clientRepository: ClientRepository) {
    suspend operator fun invoke(clientId: Int): Client? {
        return clientRepository.getClientById(clientId)?.toDomain()
    }
}