package com.example.motionpath.model

import androidx.room.Embedded
import androidx.room.Ignore
import java.util.*

data class CalendarDay(
    @Embedded val date: Date,
    @Ignore var isSelected: Boolean = false
)