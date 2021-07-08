package com.example.motionpath.domain.usecase

import com.example.motionpath.domain.SessionRepository
import com.example.motionpath.domain.mapper.SessionToEntityMapper
import com.example.motionpath.model.domain.Session

class DeleteSessionUseCase(private val sessionRepository: SessionRepository) {
    suspend operator fun invoke(session: Session) {
        sessionRepository.daleteSession(SessionToEntityMapper().invoke(session))
    }
}