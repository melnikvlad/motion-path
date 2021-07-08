package com.example.motionpath.domain.usecase

import com.example.motionpath.domain.SessionRepository
import com.example.motionpath.domain.mapper.SessionToDomainMapper
import com.example.motionpath.model.domain.Session
import com.example.motionpath.model.domain.SessionTime
import com.example.motionpath.model.domain.SessionType
import com.example.motionpath.model.entity.SessionEntity
import com.example.motionpath.util.CalendarManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class GetSessionsUseCase(private val sessionRepository: SessionRepository) {
    operator fun invoke(date: Date): Flow<List<Session>> {
        return sessionRepository.getSessions()
            .map { filterBySelectedDate(it, date) }
            .map { createFreeSchedule(date, it) }
            .map { sortByDate(it) }
    }

    private fun filterBySelectedDate(list: List<SessionEntity>, date: Date): List<Session> {
        return list.map { sessionEntity ->  SessionToDomainMapper().invoke(sessionEntity) }
            .filter {session -> session.isSameDay(date) }
    }

    private fun sortByDate(list: List<Session>): List<Session> {
        return list.sortedBy { session ->  session.time.start }
    }

    private fun createFreeSchedule(date: Date, sessions: List<Session>): List<Session> {
        val schedule = mutableListOf<Session>()
        val freeSessionTimes = mutableListOf<Session>()

        val freeDayDates = CalendarManager.getFreeDayDividedByHours(date)

        freeDayDates.forEachIndexed { index, freeDate ->
            if (index < freeDayDates.size - 1) {
                freeSessionTimes.add(
                   Session(
                       null,
                       SessionType.FREE,
                       false,
                       SessionTime(start = freeDate, finish = freeDayDates[index + 1]),
                       null
                   )
                )
            }
        }

        schedule.addAll(freeSessionTimes)
        schedule.addAll(sessions)


        return schedule
    }
}