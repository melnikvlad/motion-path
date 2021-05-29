package com.example.motionpath.ui.main

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.motionpath.data.CalendarPagingSource
import com.example.motionpath.data.CalendarRepository.Companion.PAGE_SIZE
import com.example.motionpath.data.db.ClientDao
import com.example.motionpath.model.CalendarDay
import com.example.motionpath.model.entity.ClientEntity
import com.example.motionpath.util.Logger
import com.example.motionpath.util.isSameDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(private val clientDao: ClientDao) : ViewModel() {

    val today = GregorianCalendar().time

    private val _selectedDate: MutableStateFlow<Date> = MutableStateFlow(today)
    val selectedDate = _selectedDate.asLiveData()

    private val _calendarDays: MutableLiveData<PagingData<CalendarDay>> = MutableLiveData()
    val calendarDays = _calendarDays

    @ExperimentalCoroutinesApi
    private val clientsFlow = _selectedDate.flatMapLatest { date ->
        clientDao.get().map { list ->
            list.filter { it.calendarDay.isSameDay(date) }
        }
    }
    @ExperimentalCoroutinesApi
    val clients = clientsFlow.asLiveData()

    private val calendarPagingSource = CalendarPagingSource(today)
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

    fun createSession(date: Date) {
        viewModelScope.launch {
            clientDao.insert(ClientEntity(null, "Vlad", "Melnikov", date))
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