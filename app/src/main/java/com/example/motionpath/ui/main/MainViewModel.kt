package com.example.motionpath.ui.main

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.motionpath.data.calendar.CalendarPagingSource
import com.example.motionpath.data.calendar.CalendarRepository.Companion.PAGE_SIZE
import com.example.motionpath.domain.usecase.CreateSessionUseCase
import com.example.motionpath.domain.usecase.DeleteSessionUseCase
import com.example.motionpath.domain.usecase.GetSessionsUseCase
import com.example.motionpath.model.CalendarDay
import com.example.motionpath.model.domain.Session
import com.example.motionpath.model.domain.SessionClient
import com.example.motionpath.model.domain.SessionTime
import com.example.motionpath.util.Logger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
class MainViewModel(
    private val getSessionsUseCase: GetSessionsUseCase,
    private val createSessionUseCase: CreateSessionUseCase,
    private val deleteSessionUseCase: DeleteSessionUseCase
) : ViewModel() {
    val today: Date = GregorianCalendar().time

    private val _selectedDate: MutableStateFlow<Date> = MutableStateFlow(today)
    val selectedDate = _selectedDate.asLiveData()

    private val _calendarDays: MutableLiveData<PagingData<CalendarDay>> = MutableLiveData()
    val calendarDays = _calendarDays

    private val sessionsFlow = _selectedDate.flatMapLatest { getSessionsUseCase(it) }
    val sessions = sessionsFlow.asLiveData()

    private val calendarPagingSource =
        CalendarPagingSource(today)
    private val calendarDaysFlow: Flow<PagingData<CalendarDay>> = Pager(
        config = getCalendarPagingConfig(),
        pagingSourceFactory = { calendarPagingSource }
    ).flow.cachedIn(viewModelScope)

    init {
        loadCalendar()
    }

    private fun loadCalendar() {
        viewModelScope.launch {
            calendarDaysFlow
                .catch { Logger.log(it) }
                .collect { _calendarDays.value = it }
        }
    }

    fun deleteSession(session: Session) {
        viewModelScope.launch {
            deleteSessionUseCase(session)
        }
    }

    fun selectDate(newSelectedDate: Date) {
        viewModelScope.launch {
            _selectedDate.emit(newSelectedDate)
        }
    }

    private fun getCalendarPagingConfig(): PagingConfig {
        return PagingConfig(pageSize = PAGE_SIZE / 3, enablePlaceholders = false)
    }
}