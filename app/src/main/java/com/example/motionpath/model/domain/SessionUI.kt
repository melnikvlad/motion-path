package com.example.motionpath.model.domain

open class BaseSessionUI(session: Session? = null)

data class SessionUI(
    val session: Session?,
    val isSelected: Boolean = false,
    var canShowTime: Boolean = false
) : BaseSessionUI(session) {

    fun isFree(): Boolean = session == null || session.type == SessionType.FREE
}