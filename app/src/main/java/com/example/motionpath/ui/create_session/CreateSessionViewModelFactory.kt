package com.example.motionpath.ui.create_session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.motionpath.domain.usecase.CreateSessionUseCase

class CreateSessionViewModelFactory(private val createSessionUseCase: CreateSessionUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateSessionViewModel(createSessionUseCase) as T
    }
}