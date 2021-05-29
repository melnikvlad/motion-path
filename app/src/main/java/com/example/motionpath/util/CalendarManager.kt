package com.example.motionpath.util

import android.annotation.SuppressLint
import com.example.motionpath.model.CalendarDay
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object CalendarManager {

    const val DEFAULT_FORMAT = "dd-MM-yyyy hh:mm:ss"
    const val MONTH_DAY_FORMAT = "MMMM dd"
    const val MONTH_YEAR_FORMAT = "MMMM yyyy"

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

fun Date.getWeekDay(): String {
    val sdf = SimpleDateFormat("EEE")
    return sdf.format(this)
}

fun Date.getMonthDay(): String {
    val dateFormat: DateFormat = SimpleDateFormat("dd")
    return dateFormat.format(this)
}

fun Date.toStringFormat(stringFormat: String = CalendarManager.DEFAULT_FORMAT): String {
    val dateFormat = SimpleDateFormat(stringFormat, Locale.getDefault())
    return try {
        dateFormat.format(this)
    } catch (e: ParseException) {
        e.printStackTrace()
        "N/A"
    }
}

fun Date.isToday(): Boolean {
    val calendar = Calendar.getInstance()
    val toCompare = Calendar.getInstance()
    toCompare.timeInMillis = this.time
    return calendar[Calendar.YEAR] == toCompare[Calendar.YEAR]
            && calendar[Calendar.MONTH] == toCompare[Calendar.MONTH]
            && calendar[Calendar.DAY_OF_MONTH] == toCompare[Calendar.DAY_OF_MONTH]
}

fun Date.isSameDay(toCompare: Date): Boolean {
    val cal1 = Calendar.getInstance()
    val cal2 = Calendar.getInstance()
    cal1.time = this
    cal2.time = toCompare
   return cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR] &&
                cal1[Calendar.YEAR] == cal2[Calendar.YEAR]
}

@SuppressLint("SimpleDateFormat")
fun String.toDate(): Date? {
    val format = SimpleDateFormat(CalendarManager.DEFAULT_FORMAT)
    try {
        return format.parse(this)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
}