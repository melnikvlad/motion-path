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
    const val HOUR_MiNUTE_FORMAT = "HH:mm"
    const val DAY_MONTH_WEEKDAY = "dd MMMM, EEEE"

    fun getDaysAfter(startDate: Date, count: Int): List<CalendarDay> {
        val datesInRange = mutableListOf<CalendarDay>()

        val startCalendar = GregorianCalendar()
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

        val startCalendar = GregorianCalendar()
        startCalendar.time = startDate
        startCalendar.add(Calendar.DATE, -1)

        val endCalendar = GregorianCalendar()
        endCalendar.time = startDate
        endCalendar.add(Calendar.DATE, (count - 1).unaryMinus())

        while (startCalendar.after(endCalendar)) {
            datesInRange.add(CalendarDay(startCalendar.time))
            startCalendar.add(Calendar.DATE, -1)
        }
        return datesInRange
    }

    fun getDayDividedByHours(startDate: Date, endDate: Date? = null): List<Date> {
        val dates = mutableListOf<Date>()

        val startCalendar = Calendar.getInstance()
        startCalendar.time = startDate

        val endCalendar = Calendar.getInstance()
        if (endDate == null) {
            endCalendar.time = startCalendar.time
            endCalendar.add(Calendar.HOUR_OF_DAY, 19)
        } else {
            endCalendar.time = endDate
        }
        //endCalendar.set(Calendar.MINUTE, 0)

        while (startCalendar.before(endCalendar)) {
            dates.add(startCalendar.time)
            startCalendar.add(Calendar.HOUR_OF_DAY, 1)
        }

        return dates
    }

    fun getHoursBetweenDates(startDate: Date, endDate: Date): List<Date> {
        var forceQuit = false
        val dates = mutableListOf<Date>()

        val startCalendar = Calendar.getInstance()
        startCalendar.time = startDate
        startCalendar.set(Calendar.SECOND, 0)

        val endCalendar = Calendar.getInstance()
        endCalendar.time = endDate
        endCalendar.set(Calendar.SECOND, 0)

        while (startCalendar.before(endCalendar) && !forceQuit) {
            val difference = getDifference(startCalendar.time, endCalendar.time)
            val hasHourBetween = difference.second >= 1
            if (hasHourBetween) {
                if (startCalendar.get(Calendar.MINUTE) > 0) {
                    startCalendar.add(Calendar.MINUTE, 60 - startCalendar.get(Calendar.MINUTE))
                } else {
                    startCalendar.add(Calendar.HOUR_OF_DAY, 1)
                }
                dates.add(startCalendar.time)
            } else {
                startCalendar.add(Calendar.MINUTE, difference.third.toInt())
                dates.add(startCalendar.time)
                forceQuit = true
            }
        }

        return dates
    }
}

fun Date.getStartOfWorkDay(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, 6)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    return cal.time
}

fun Date.getEndOfDay(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, 23)
    cal.set(Calendar.MINUTE, 59)
    cal.set(Calendar.SECOND, 0)
    return cal.time
}

fun Date.getStartOfDay(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    return cal.time
}

fun getDifference(startDate: Date, endDate: Date): Triple<Long, Long, Long> {
    var different = endDate.time - startDate.time
    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24

    val elapsedDays = different / daysInMilli
    different %= daysInMilli
    val elapsedHours = different / hoursInMilli
    different = different % hoursInMilli
    val elapsedMinutes = different / minutesInMilli

    return Triple(elapsedDays, elapsedHours, elapsedMinutes)
}

fun Date.getWeekDay(): String {
    val sdf = SimpleDateFormat("EEE")
    return sdf.format(this)
}

fun Date.getMonthDay(): String {
    val dateFormat: DateFormat = SimpleDateFormat("d")
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

fun Date.plusHour(count: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.HOUR_OF_DAY, count)
    return calendar.time
}

fun Date.setDay(newDate: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this

    val newCalendar = Calendar.getInstance()
    newCalendar.time = newDate
    newCalendar.set(Calendar.HOUR, calendar[Calendar.HOUR_OF_DAY])
    newCalendar.set(Calendar.MINUTE, calendar[Calendar.MINUTE])
    return newCalendar.time
}

fun Date.newTime(hour: Int, minutes: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minutes)
    return calendar.time
}

fun Date.setHour(value: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, value)
    return calendar.time
}

fun Date.setMinutesValue(value: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.MINUTE, value)
    return calendar.time
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