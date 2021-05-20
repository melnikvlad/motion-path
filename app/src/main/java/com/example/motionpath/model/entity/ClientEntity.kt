package com.example.motionpath.model.entity

import androidx.room.*
import com.example.motionpath.model.CalendarDay
import com.example.motionpath.ui.main.adapter.toStringFormat
import java.util.*

@Entity(tableName = "clients")
data class ClientEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "lastName") val lastName: String,
    @ColumnInfo(name = "date") @TypeConverters(DateConverter::class) val calendarDay: Date
) {
    override fun toString(): String {
        return "${name}: ${calendarDay.toStringFormat()}"
    }
}

class CalendarDayConverter {

    @TypeConverter
    fun fromCalendarDay(calendarDay: CalendarDay): Long {
        return calendarDay.date.time
    }

    @TypeConverter
    fun toCalendarDay(time: Long): CalendarDay {
        return CalendarDay(Date(time))
    }
}

class DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}