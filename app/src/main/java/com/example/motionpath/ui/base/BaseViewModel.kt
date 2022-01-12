package com.example.motionpath.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S, A, E>: ViewModel() {

    private val _viewStates = MutableStateFlow<S?>(null)
    fun viewStates(): StateFlow<S?> = _viewStates

    private val _viewActions = MutableSharedFlow<A?>()
    fun viewActions(): SharedFlow<A?> = _viewActions

    private var _viewState: S? = null
    protected var viewState: S
        get() = _viewState
            ?: throw UninitializedPropertyAccessException("\"viewState\" was queried before being initialized")
        set(value) {
            _viewState = value
            _viewStates.value = value
        }

    private var _viewAction: A? = null
    protected var viewAction: A
        get() = _viewAction
            ?: throw UninitializedPropertyAccessException("\"viewAction\" was queried before being initialized")
        set(value) {
            _viewAction = value
            viewModelScope.launch {
                _viewActions.emit(value)
            }
        }

    abstract fun processEvent(event: E)
}