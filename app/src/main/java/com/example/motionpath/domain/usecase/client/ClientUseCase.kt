package com.example.motionpath.domain.usecase.client

data class ClientUseCase(
    val getClient: GetClientUseCase,
    val getClients: GetClientsUseCase,
    val getClientsWithTrains: GetClientsWithTrainsUseCase, /* unused */
    val createClient: CreateClientUseCase
)
