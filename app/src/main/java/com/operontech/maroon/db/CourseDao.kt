package com.operontech.maroon.db

import android.arch.persistence.room.*

@Dao
interface CourseDao {
    @Query("SELECT * FROM course")
    fun getAllCourses(): List<Course>

    @Query("SELECT * FROM course WHERE courseID = :courseID")
    fun getByID(courseID: Long): Course

    @Query("SELECT * FROM course WHERE courseID IN (:courseIDs)")
    fun getAllByID(courseIDs: List<Long>): List<Course>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourse(course: Course)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateCourse(course: Course)

    @Delete
    fun deleteCourse(course: Course)
}