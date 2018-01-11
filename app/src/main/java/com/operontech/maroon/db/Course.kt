package com.operontech.maroon.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Course(
        @PrimaryKey(autoGenerate = true)
        var courseID: Long,
        var subject: String,
        var number: Int,
        var section: String,
        var title: String,
        var credits: Int,
        var instructor: String
)
