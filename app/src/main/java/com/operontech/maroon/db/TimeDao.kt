package com.operontech.maroon.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface TimeDao {
    @Query("SELECT * FROM time")
    fun getAllTimes(): List<Time>

    @Query("SELECT * FROM time WHERE timeID = :timeID LIMIT 1")
    fun getTimeByID(timeID: Long): Time

    @Query("SELECT * FROM time WHERE courseID = :courseID")
    fun getTimesByCourseID(courseID: Long): List<Time>

    @Query("SELECT courseID FROM time WHERE :day = 1")
    fun getCourseIDsByDayOfWeek(day: DayOfWeek): List<Course>

    @Query("SELECT course.* " +
            "FROM course JOIN time " +
            "ON course.courseID = time.courseID" +
            "WHERE time.:day = 1")
    fun getCoursesByDayOfWeek(day: DayOfWeek): List<Course>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun addTime(time: Time)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTime(time: Time)

    fun deleteTime(time: Time)
}