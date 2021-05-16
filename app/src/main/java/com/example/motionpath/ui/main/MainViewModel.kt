package com.example.motionpath.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.motionpath.data.CalendarRepository
import com.example.motionpath.model.CalendarDay
import com.example.motionpath.util.Logger
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {

    private val _calendarDays: MutableLiveData<PagingData<CalendarDay>> = MutableLiveData()
    val calendarDays = _calendarDays

    private val repository = CalendarRepository()

    init {
        loadCalendar(date = GregorianCalendar().time)
    }

    private fun loadCalendar(date: Date) {
        viewModelScope.launch {
            repository.getCalendarDays(date)
                .catch { Logger.log(it) }
                .collect { _calendarDays.value = it }
        }
    }
}