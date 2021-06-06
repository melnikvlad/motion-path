package com.example.motionpath.util

import android.util.Log
import androidx.annotation.StringRes

object Logger {
    private const val TAG = "TST_TST"

    fun log(error: Throwable) {
        Log.d(TAG, error.message.toString())
    }

    fun log(message: String) {
        Log.d(TAG, message)
    }
}