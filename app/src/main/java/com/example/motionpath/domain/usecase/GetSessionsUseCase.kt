package com.example.motionpath.domain.usecase

import com.example.motionpath.domain.SessionRepository
import com.example.motionpath.domain.mapper.SessionToDomainMapper
import com.example.motionpath.model.domain.Session
import com.example.motionpath.model.domain.SessionTime
import com.example.motionpath.model.domain.SessionType
import com.example.motionpath.model.domain.SessionUI
import com.example.motionpath.model.entity.SessionEntity
import com.example.motionpath.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import kotlin.collections.ArrayList

class GetSessionsUseCase(private val sessionRepository: SessionRepository) {
    operator fun invoke(date: Date): Flow<List<SessionUI>> {
        return sessionRepository.getSessions()
            .map { filterBySelectedDate(it, date) }
            .map { sortByDate(it) }
            .map { createFreeSchedule(date, it) }
    }

    private fun filterBySelectedDate(list: List<SessionEntity>, date: Date): List<Session> {
        return list.map { sessionEntity -> SessionToDomainMapper().invoke(sessionEntity) }
            .filter { session -> session.isSameDay(date) }
    }

    private fun sortByDate(list: List<Session>): List<Session> {
        return list.sortedBy { session -> session.time.start }
    }

    private fun createFreeSchedule(date: Date, sessions: List<Session>): List<SessionUI> {
        val schedule = ArrayList<SessionUI>()
        val freeSessionTimes = mutableListOf<SessionUI>()

        val size = sessions.size
        sessions.forEachIndexed { index, session ->
            val next = if (index + 1 < size) {
                sessions[index + 1]
            } else {
                schedule.add(SessionUI(session, canShowTime = true))
                return schedule
            }

            val (start, end) = Pair(session.time.finish, next.time.start)

            CalendarManager.getHoursBetweenDates(start, end).forEach { date ->
                val sessionUI = SessionUI(
                    session = Session(
                        null,
                        SessionType.FREE,
                        false,
                        SessionTime(start = date, finish = date),
                        null
                    ),
                    canShowTime = true
                )
                freeSessionTimes.add(sessionUI)
            }
            schedule.add(SessionUI(session, canShowTime = true))
            schedule.addAll(freeSessionTimes)
            freeSessionTimes.clear()
        }

        return schedule
    }
}