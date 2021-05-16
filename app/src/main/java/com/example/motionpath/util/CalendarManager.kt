package com.example.motionpath.util

import com.example.motionpath.model.CalendarDay
import java.util.*

object CalendarManager {

    fun getDaysAfter(startDate: Date, count: Int): List<CalendarDay> {
        val datesInRange = mutableListOf<CalendarDay>()

        val startCalendar: Calendar = GregorianCalendar()
        startCalendar.time = startDate

        val endCalendar = GregorianCalendar()
        endCalendar.time = startDate
        endCalendar.add(Calendar.DATE, count)

        while (startCalendar.before(endCalendar)) {
            val calendarDay = CalendarDay(startCalendar.time)
            datesInRange.add(calendarDay)
            startCalendar.add(Calendar.DATE, 1)
        }

        return datesInRange
    }

    fun getDaysBefore(startDate: Date, count: Int): List<CalendarDay> {
        val datesInRange = mutableListOf<CalendarDay>()

        val startCalendar: Calendar = GregorianCalendar()
        startCalendar.time = startDate
        startCalendar.add(Calendar.DATE, -1)

        val endCalendar = GregorianCalendar()
        endCalendar.time = startDate
        endCalendar.add(Calendar.DATE, count.unaryMinus())

        while (startCalendar.after(endCalendar)) {
            datesInRange.add(CalendarDay(startCalendar.time))
            startCalendar.add(Calendar.DATE, -1)
        }
        return datesInRange
    }

}