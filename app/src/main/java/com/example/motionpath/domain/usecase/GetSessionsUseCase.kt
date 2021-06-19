package com.example.motionpath.domain.usecase

import com.example.motionpath.domain.SessionRepository
import com.example.motionpath.domain.mapper.SessionToDomainMapper
import com.example.motionpath.model.domain.*
import com.example.motionpath.model.entity.SessionEntity
import com.example.motionpath.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import kotlin.collections.ArrayList

class GetSessionsUseCase(private val sessionRepository: SessionRepository) {
    operator fun invoke(date: Date): Flow<List<BaseSessionUI>> {
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

    private fun createFreeSchedule(date: Date, sessions: List<Session>): List<BaseSessionUI> {
        val schedule = ArrayList<BaseSessionUI>()
        val freeSessionTimes = mutableListOf<BaseSessionUI>()

        val size = sessions.size
        sessions.forEachIndexed { index, session ->
            val next = if (index + 1 < size) {
                sessions[index + 1]
            } else {
                schedule.add(SessionUI(session))
                CalendarManager.getHoursBetweenDates(sessions.last().time.finish, date.getEndOfWorkDay()).forEach {
                    freeSessionTimes.add(FreeSessionUI(displayTime = true, time = it))
                }

                schedule.addAll(freeSessionTimes)
                return schedule
            }

            val (start, end) = Pair(session.time.finish, next.time.start)

            CalendarManager.getHoursBetweenDates(start, end).forEach { date ->
                freeSessionTimes.add(FreeSessionUI(displayTime = true, time = date))
            }
            freeSessionTimes.lastOrNull()?.canShowTime = false
            schedule.add(SessionUI(session))
            schedule.addAll(freeSessionTimes)
            freeSessionTimes.clear()
        }

        return schedule
    }
}