package com.operontech.maroon.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey

@Entity(indices = arrayOf(Index(value = "courseID")))
data class Time(
        @PrimaryKey(autoGenerate = true)
        var timeID: Long,

        @ForeignKey(
                entity = Course::class,
                parentColumns = arrayOf("courseID"),
                childColumns = arrayOf("courseID"),
                onDelete = ForeignKey.CASCADE)
        var courseID: Long,

        // Time represented in milliseconds in Epoch
        var startTime: Long,
        var endTime: Long,

        // building, room information
        var building: String,
        var room: String,

        // days of the week
        var monday: Boolean,
        var tuesday: Boolean,
        var wednesday: Boolean,
        var thursday: Boolean,
        var friday: Boolean,
        var saturday: Boolean,
        var sunday: Boolean
)
