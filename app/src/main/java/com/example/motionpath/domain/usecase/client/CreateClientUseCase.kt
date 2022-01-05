package com.example.motionpath.domain.usecase.client

import com.example.motionpath.domain.ClientRepository
import com.example.motionpath.model.entity.ClientEntity

class CreateClientUseCase(private val clientRepository: ClientRepository) {
    suspend operator fun invoke(name: String, description: String, goal: String): Long {
        return clientRepository.createClient(
            ClientEntity(name = name, description = description, goal = goal)
        )
    }
}