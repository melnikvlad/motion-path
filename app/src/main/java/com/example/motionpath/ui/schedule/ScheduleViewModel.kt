package com.example.motionpath.ui.schedule

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.motionpath.data.calendar.CalendarPagingSource
import com.example.motionpath.data.calendar.CalendarRepository.Companion.PAGE_SIZE
import com.example.motionpath.domain.usecase.client.ClientUseCase
import com.example.motionpath.domain.usecase.train.*
import com.example.motionpath.model.CalendarDay
import com.example.motionpath.model.entity.TrainEntity
import com.example.motionpath.util.Logger
import com.example.motionpath.util.extension.executeIO
import com.example.motionpath.util.getEndOfDay
import com.example.motionpath.util.getStartOfDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val trainUseCase: TrainUseCase
) : ViewModel() {
    val today: Date = Calendar.getInstance().time

    private val _selectedDate: MutableStateFlow<Date> = MutableStateFlow(today)
    val selectedDate = _selectedDate.asLiveData()

    private val _calendarDays: MutableLiveData<PagingData<CalendarDay>> = MutableLiveData()
    val calendarDays = _calendarDays

    private val calendarPagingSource = CalendarPagingSource(today)
    private val calendarDaysFlow: Flow<PagingData<CalendarDay>> = Pager(
        config = getCalendarPagingConfig(),
        pagingSourceFactory = { calendarPagingSource }
    ).flow.cachedIn(viewModelScope)

    private val trainsFlow = _selectedDate.flatMapLatest {
        trainUseCase.getTrainForDate(it.getStartOfDay().time, it.getEndOfDay().time) // TODO leave only 1 param (Date)
    }
    val trains = trainsFlow.asLiveData()

    init {
        loadCalendar()
    }

    fun deleteTrain(train: TrainEntity) {
        viewModelScope.executeIO {
            trainUseCase.deleteTrain(train)
        }
    }

    fun deleteAllClientTrains(clientId: Int) {
        viewModelScope.executeIO {
            trainUseCase.deleteClientTrainsUseCase.invoke(clientId)
        }
    }

    private fun loadCalendar() {
        viewModelScope.launch {
            calendarDaysFlow
                .catch { Logger.log(it) }
                .collect { _calendarDays.value = it }
        }
    }

    fun selectDate(newSelectedDate: Date) {
        if (_selectedDate.value == newSelectedDate) return

        viewModelScope.launch {
            _selectedDate.emit(newSelectedDate)
        }
    }

    private fun getCalendarPagingConfig(): PagingConfig {
        return PagingConfig(pageSize = PAGE_SIZE / 3, enablePlaceholders = false)
    }
}