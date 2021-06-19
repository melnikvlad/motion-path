package com.example.motionpath.model.domain

import java.util.*

open class BaseSessionUI(
    val type: SessionType,
    protected val isSelected: Boolean,
    var canShowTime: Boolean,
    val session: Session? = null
)

class SessionUI(session: Session?) : BaseSessionUI(SessionType.CLIENT, false, false, session) {

    fun isFree(): Boolean = session == null || session.type == SessionType.FREE
}

data class FreeSessionUI(val displayTime: Boolean, val time: Date?): BaseSessionUI(SessionType.FREE, false, canShowTime = displayTime)