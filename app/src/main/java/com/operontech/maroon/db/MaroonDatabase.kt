package com.operontech.maroon.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(
        entities = arrayOf(Course::class, Time::class),
        version = 1,
        exportSchema = false
)
abstract class MaroonDatabase : RoomDatabase() {

    abstract fun CourseDao(): CourseDao
    abstract fun TimeDao(): TimeDao

}