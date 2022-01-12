package com.example.motionpath.domain.usecase.client

import com.example.motionpath.data.model.entity.ClientEntity
import com.example.motionpath.domain.ClientRepository

class CreateClientUseCase(private val clientRepository: ClientRepository) {
    suspend operator fun invoke(name: String, description: String, goal: String): Long {
        return clientRepository.createClient(
            ClientEntity(name = name, description = description, goal = goal)
        )
    }
}