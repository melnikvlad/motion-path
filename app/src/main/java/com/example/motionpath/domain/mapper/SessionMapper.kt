package com.example.motionpath.domain.mapper

import com.example.motionpath.domain.AbstractMapper
import com.example.motionpath.model.domain.Session
import com.example.motionpath.model.domain.SessionType
import com.example.motionpath.model.entity.SessionEntity

class SessionToDomainMapper : AbstractMapper<SessionEntity, Session>() {
    override fun map(model: SessionEntity): Session {
        return Session(
            id = model.id,
            type = SessionType.CLIENT,
            notifyEnabled = model.notifyEnabled,
            time = model.time,
            client = model.client,
            activities = model.activities
        )
    }
}

class SessionToEntityMapper : AbstractMapper<Session, SessionEntity>() {
    override fun map(model: Session): SessionEntity {
        return SessionEntity(
            id = model.id,
            notifyEnabled = model.notifyEnabled,
            time = model.time,
            client = model.client,
            activities = model.activities
        )
    }
}