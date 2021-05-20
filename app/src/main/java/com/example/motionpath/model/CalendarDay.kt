package com.example.motionpath.model

import androidx.room.Embedded
import java.util.*

data class CalendarDay(@Embedded val date: Date)