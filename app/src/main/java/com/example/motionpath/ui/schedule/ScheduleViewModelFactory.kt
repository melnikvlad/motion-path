package com.example.motionpath.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.motionpath.domain.usecase.DeleteSessionUseCase
import com.example.motionpath.domain.usecase.GetSessionsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ScheduleViewModelFactory(
    private val getSessionsUseCase: GetSessionsUseCase,
    private val deleteSessionUseCase: DeleteSessionUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ScheduleViewModel(getSessionsUseCase, deleteSessionUseCase) as T
    }
}