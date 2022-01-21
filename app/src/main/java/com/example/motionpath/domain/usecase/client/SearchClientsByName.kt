package com.example.motionpath.domain.usecase.client

import com.example.motionpath.data.model.entity.toDomain
import com.example.motionpath.domain.ClientRepository
import com.example.motionpath.ui.create_train.Client
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class SearchClientsByName(private val repository: ClientRepository) {

    operator fun invoke(query: String): Flow<List<Client>> {
        return repository.searchClients(query)
            .map { list ->
                list.map { it.toDomain() }
            }
            .flowOn(Dispatchers.IO)
    }
}