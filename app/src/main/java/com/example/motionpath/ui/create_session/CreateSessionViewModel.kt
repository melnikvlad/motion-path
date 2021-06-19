package com.example.motionpath.ui.create_session

import android.os.Bundle
import androidx.lifecycle.*
import com.example.motionpath.domain.usecase.CreateSessionUseCase
import com.example.motionpath.model.domain.Session
import com.example.motionpath.model.domain.SessionClient
import com.example.motionpath.model.domain.SessionTime
import com.example.motionpath.ui.schedule.ScheduleFragment
import com.example.motionpath.util.plusHour
import com.example.motionpath.util.setHour
import com.example.motionpath.util.setMinutesValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
class CreateSessionViewModel(
    private val createSessionUseCase: CreateSessionUseCase,
    args: Bundle?
) : ViewModel() {

    private val _sessionDate: MutableStateFlow<Date?> =
        MutableStateFlow(args?.getSerializable(ScheduleFragment.KEY_SELECTED_DATE) as? Date)
    val sessionDate = _sessionDate.asLiveData()

    var timePickerState = TimePickerState.PICK_START

    fun createSession() {
        _sessionDate.value?.let {
            createSessionAt(it)
        }
    }

    fun createSessionAt(date: Date) {
        viewModelScope.launch {
            val session = Session(
                time = SessionTime(date, date.plusHour(1)),
                client = SessionClient(
                    id = 1,
                    name = "Olya",
                    lastName = "Melnikova",
                    type = 1,
                    comment = "Болит спина ужас как сильно",
                    sessionsPassed = 4,
                    packageSessionsCount = 5
                ),
                activities = emptyList()
            )
            createSessionUseCase(session)
        }
    }

    fun updateSessionDate(date: Date) {
        viewModelScope.launch {
            _sessionDate.emit(date)
        }
    }

    fun updateSessionTime(hour: Int, minute: Int) {
        timePickerState = when (timePickerState) {
            TimePickerState.PICK_START -> TimePickerState.PICK_END
            else -> TimePickerState.PICK_START
        }

        _sessionDate.value?.let {
            var date = it
            date = date.setHour(hour)
            date = date.setMinutesValue(minute)
            updateSessionDate(date)
        }
    }
}

enum class TimePickerState {
    PICK_START,
    PICK_END
}