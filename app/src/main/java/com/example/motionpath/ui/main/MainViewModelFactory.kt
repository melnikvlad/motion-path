package com.example.motionpath.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.motionpath.domain.usecase.CreateSessionUseCase
import com.example.motionpath.domain.usecase.DeleteSessionUseCase
import com.example.motionpath.domain.usecase.GetSessionsUseCase

class MainViewModelFactory(
    private val getSessionsUseCase: GetSessionsUseCase,
    private val createSessionUseCase: CreateSessionUseCase,
    private val deleteSessionUseCase: DeleteSessionUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(getSessionsUseCase, createSessionUseCase, deleteSessionUseCase) as T
    }
}