package com.operontech.maroon.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CourseDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    interface COLUMNS {
        companion object {
            val ID = "id"

            val COURSE_SUBJECT = "course_subject"
            val COURSE_NUMBER = "course_number"
            val COURSE_SECTION = "course_section"
            val COURSE_TITLE = "course_title"

            val COURSE_CREDIT_HOURS = "course_credit_hours"

            val COURSE_BUILDING = "course_building"
            val COURSE_ROOM_NUMBER = "course_room_number"

            val COURSE_INSTRUCTOR = "course_instructor"

            val COURSE_TIMES = "course_times"
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // @formatter:off
		db.execSQL(
				"CREATE TABLE " + P_TABLE + " ( " +
				COLUMNS.ID + " INTEGER NOT NULL PRIMARY KEY, " +
				COLUMNS.COURSE_SUBJECT + " TEXT DEFAULT '', " +
				COLUMNS.COURSE_NUMBER + " TEXT DEFAULT '0', " +
				COLUMNS.COURSE_SECTION + " TEXT DEFAULT '0', " +
				COLUMNS.COURSE_TITLE + " TEXT DEFAULT '', " +
				COLUMNS.COURSE_CREDIT_HOURS + " TEXT DEFAULT '0', " +

				COLUMNS.COURSE_BUILDING + " TEXT DEFAULT '', " +
				COLUMNS.COURSE_ROOM_NUMBER + " TEXT DEFAULT '', " +

				COLUMNS.COURSE_INSTRUCTOR + " TEXT DEFAULT '', " +

				COLUMNS.COURSE_TIMES + " TEXT DEFAULT ''")
		// @formatter:on
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        //TODO: Implement upgrades
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "courseDatabase.db"
        private val P_TABLE = "main_schedule"
    }
}
