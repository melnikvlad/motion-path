package com.example.motionpath.util

import android.util.Log

object Logger {
    private const val TAG = "TST_TST"

    fun log(error: Throwable) {
        Log.d(TAG, error.message.toString())
    }
}