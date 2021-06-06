package com.example.motionpath.model.entity

import androidx.room.*
import com.example.motionpath.model.domain.SessionActivity
import com.example.motionpath.model.domain.SessionClient
import com.example.motionpath.model.domain.SessionTime
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*


@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "notifyEnabled") val notifyEnabled: Boolean = false,
    @ColumnInfo(name = "sessionTime") @TypeConverters(SessionTimeConverter::class) val time: SessionTime,
    @ColumnInfo(name = "sessionClient") @TypeConverters(SessionClientConverter::class) val client: SessionClient?,
    @ColumnInfo(name = "sessionActivities") @TypeConverters(SessionActivityConverter::class) val activities: List<SessionActivity>
)

class DateConverter {
    @TypeConverter
    fun fromDate(value: Long?): Date? = value?.let { Date(it) }
    @TypeConverter
    fun toDate(date: Date?): Long? = date?.time
}

class SessionTimeConverter {
    @TypeConverter
    fun fromSessionTime(value: SessionTime) = Gson().toJson(value)

    @TypeConverter
    fun toSessionTime(value: String) = Gson().fromJson(value, SessionTime::class.java)
}

class SessionClientConverter {
    @TypeConverter
    fun fromSessionClient(value: SessionClient) = Gson().toJson(value)

    @TypeConverter
    fun toSessionClient(value: String) = Gson().fromJson(value, SessionClient::class.java)
}

class SessionActivityConverter {
    @TypeConverter
    fun fromSessionActivity(value: List<SessionActivity>): String? {
        val type: Type = object : TypeToken<List<SessionActivity?>?>() {}.type
        return Gson().toJson(value, type)
    }

    @TypeConverter
    fun toSessionActivity(value: String): List<SessionActivity>? {
        val type: Type = object : TypeToken<List<SessionActivity?>?>() {}.type
        return Gson().fromJson(value, type)
    }
}