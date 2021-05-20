package com.example.motionpath.ui.main

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.example.motionpath.data.CalendarRepository
import com.example.motionpath.data.db.ClientDao
import com.example.motionpath.model.CalendarDay
import com.example.motionpath.model.entity.ClientEntity
import com.example.motionpath.util.Logger
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(private val clientDao: ClientDao) : ViewModel() {

    private val _calendarDays: MutableLiveData<PagingData<CalendarDay>> = MutableLiveData()
    val calendarDays = _calendarDays

    private val repository = CalendarRepository()

    private val clientsFlow = clientDao.getAllClients()

    val clients = clientsFlow.asLiveData()

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

    fun createClient() {
        viewModelScope.launch {
            clientDao.insertClient(ClientEntity(null,"Vlad", "Melnikov", GregorianCalendar().time))
        }
    }
}