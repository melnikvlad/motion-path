package com.example.motionpath.data.session

import com.example.motionpath.data.db.SessionDao
import com.example.motionpath.domain.SessionRepository
import com.example.motionpath.model.entity.SessionEntity
import kotlinx.coroutines.flow.Flow

class SessionRepositoryImpl(private val sessionDao: SessionDao) : SessionRepository {

    override fun getSessions(): Flow<List<SessionEntity>> {
        return sessionDao.get()
    }

    override suspend fun createSession(sessionEntity: SessionEntity) {
        sessionDao.insert(sessionEntity)
    }

    override suspend fun daleteSession(sessionEntity: SessionEntity) {
        sessionDao.delete(sessionEntity)
    }
}