package com.example.motionpath.domain.usecase.client

import com.example.motionpath.data.model.entity.ClientEntity
import com.example.motionpath.data.model.entity.toEntity
import com.example.motionpath.domain.ClientRepository
import com.example.motionpath.ui.create_train.Client

class CreateClientUseCase(private val clientRepository: ClientRepository) {
    suspend operator fun invoke(client: Client): Long {
        return clientRepository.createClient(client.toEntity())
    }
}