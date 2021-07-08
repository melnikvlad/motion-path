package com.example.motionpath.util

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import androidx.annotation.Px
import kotlin.math.roundToInt


class DeviceUtils {
    val Activity.displayMetrics: DisplayMetrics
        get() {
            val displayMetrics = DisplayMetrics()

            if (Build.VERSION.SDK_INT >= 30){
                display?.apply {
                    getRealMetrics(displayMetrics)
                }
            }else{
                windowManager.defaultDisplay.getMetrics(displayMetrics)
            }

            return displayMetrics
        }

    val Activity.screenSizeInDp: Point
        get() {
            val point = Point()
            displayMetrics.apply {
                point.x = (widthPixels / density).roundToInt()
                point.y = (heightPixels / density).roundToInt()
            }

            return point
        }
}