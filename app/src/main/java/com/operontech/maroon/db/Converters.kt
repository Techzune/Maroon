package com.operontech.maroon.db

import android.arch.persistence.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromDayOfWeek(dayOfWeek: String): DayOfWeek = DayOfWeek.valueOf(dayOfWeek)

    @TypeConverter
    fun dayOfWeekToString(dayOfWeek: DayOfWeek): String = dayOfWeek.toString()
}