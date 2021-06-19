package com.example.motionpath.ui.create_session

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.motionpath.domain.usecase.CreateSessionUseCase

class CreateSessionViewModelFactory(
    private val createSessionUseCase: CreateSessionUseCase,
    private val createSessionBundle: Bundle?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateSessionViewModel(createSessionUseCase, createSessionBundle) as T
    }
}