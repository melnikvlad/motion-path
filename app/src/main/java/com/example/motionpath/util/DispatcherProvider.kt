package com.example.motionpath.util

import kotlinx.coroutines.CoroutineDispatcher

data class DispatcherProvider(
    val IO: CoroutineDispatcher,
    val Main: CoroutineDispatcher
)
