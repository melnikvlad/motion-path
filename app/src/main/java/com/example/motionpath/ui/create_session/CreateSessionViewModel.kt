package com.example.motionpath.ui.create_session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.motionpath.domain.usecase.CreateSessionUseCase
import com.example.motionpath.model.domain.Session
import com.example.motionpath.model.domain.SessionClient
import com.example.motionpath.model.domain.SessionTime
import kotlinx.coroutines.launch
import java.util.*

class CreateSessionViewModel(
    private val createSessionUseCase: CreateSessionUseCase
) : ViewModel() {

    fun createSessionAt(date: Date) {
        viewModelScope.launch {
            val session = Session(
                time = SessionTime(date, date),
                client = SessionClient(
                    id = 1,
                    name = "Vlad",
                    lastName = "Melnikov",
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
}