package com.example.motionpath.util

import kotlinx.coroutines.CoroutineDispatcher

data class DispatcherProvider(
    val IoDispatcher: CoroutineDispatcher,
    val mainDispatcher: CoroutineDispatcher
)
