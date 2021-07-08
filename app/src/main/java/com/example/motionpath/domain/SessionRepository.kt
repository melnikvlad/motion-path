package com.example.motionpath.domain

import com.example.motionpath.model.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getSessions(): Flow<List<SessionEntity>>
    suspend fun createSession(sessionEntity: SessionEntity)
    suspend fun daleteSession(sessionEntity: SessionEntity)
    // TODO: get()
    // TODO: get(date: Date)
    // TODO: insert(domainModel: DomainModel)
    // TODO: deleteAll()
    // TODO: delete(domainModel: DomainModel)
    // TODO: update(domainModel: DomainModel)
}