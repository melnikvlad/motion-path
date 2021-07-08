package com.example.motionpath.model.domain

import com.example.motionpath.util.isSameDay
import com.example.motionpath.util.isToday
import java.util.Date

data class Session(
    val id: Int? = null,
    val type: SessionType = SessionType.CLIENT,
    val notifyEnabled: Boolean = false,
    val time: SessionTime,
    val client: SessionClient?,
    val activities: List<SessionActivity> = emptyList()
) {

    fun isToday() = time.start.isToday() && time.finish.isToday() && time.start.isSameDay(time.finish)

    fun isSameDay(date: Date) = time.start.isSameDay(date) && time.finish.isSameDay(date)

    fun hasActivities() = activities.isNotEmpty()

    fun isFree() = type == SessionType.FREE
}

data class SessionTime(val start: Date, val finish: Date)

data class SessionClient(
    val id: Int,
    val name: String,
    val lastName: String,
    val type: Int,
    val comment: String? = null,
    val sessionsPassed: Int = 0,
    val packageSessionsCount: Int? = null
) {
    fun getCurrentSessionsCounter() = "$sessionsPassed / $packageSessionsCount"
}

data class SessionActivity(val id: Int, val name: String, val type: Int)

enum class SessionType {
    FREE,
    CLIENT
}
