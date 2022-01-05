package com.example.motionpath.util.extension

import kotlinx.coroutines.*

suspend fun <T> switchUI(block: suspend CoroutineScope.() -> T) = withContext(Dispatchers.Main) { block() }

fun <T> CoroutineScope.executeIO(block: suspend CoroutineScope.() -> T) = this.launch(Dispatchers.IO) { block() }

fun <T> CoroutineScope.executeUI(block: suspend CoroutineScope.() -> T) = this.launch(Dispatchers.Main) { block() }
