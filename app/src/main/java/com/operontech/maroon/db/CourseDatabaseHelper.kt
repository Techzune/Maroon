package com.operontech.maroon.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.operontech.maroon.activity.ActivityMain.Companion.MAROON_TAG
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormatterBuilder

class CourseDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    interface TABLES {
        companion object {
            const val MASTER = "CourseMaster"
            const val TIMES = "CourseTimes"
        }
    }

    interface COLUMNS {
        companion object {
            const val ID = "id"

            const val MASTER_SUBJECT = "subject"
            const val MASTER_NUMBER = "number"
            const val MASTER_SECTION = "section"
            const val MASTER_TITLE = "title"
            const val MASTER_CREDIT_HOURS = "credits"
            const val MASTER_INSTRUCTOR = "instructor"

            const val TIMES_COURSE_ID = "course_id"
            const val TIMES_DAY = "day"
            const val TIMES_TIME_BEGIN = "time_begin"
            const val TIMES_TIME_END = "time_end"
            const val TIMES_COURSE_BUILDING = "building"
            const val TIMES_COURSE_ROOM = "room"
        }
    }

    private val QUERY_GET_BY_DAY = "SELECT" +
            "  m." + COLUMNS.ID +
            ", m." + COLUMNS.MASTER_SUBJECT +
            ", m." + COLUMNS.MASTER_NUMBER +
            ", m." + COLUMNS.MASTER_SECTION +
            ", m." + COLUMNS.MASTER_TITLE +
            ", m." + COLUMNS.MASTER_CREDIT_HOURS +
            ", m." + COLUMNS.MASTER_INSTRUCTOR +
            ", t." + COLUMNS.TIMES_DAY +
            ", t." + COLUMNS.TIMES_TIME_BEGIN +
            ", t." + COLUMNS.TIMES_TIME_END +
            ", t." + COLUMNS.TIMES_COURSE_BUILDING +
            ", t." + COLUMNS.TIMES_COURSE_ROOM +

            " FROM " + TABLES.MASTER + " AS m" +
            " JOIN " + TABLES.TIMES + " AS t" +
            " ON m." + COLUMNS.ID + " = t." + COLUMNS.TIMES_COURSE_ID +
            " WHERE " + COLUMNS.TIMES_DAY + "=?" +
            " ORDER BY " + COLUMNS.MASTER_TITLE + " ASC"

    private val QUERY_GET_ALL_MASTER = "SELECT" +
            " * FROM " + TABLES.MASTER +
            " ORDER BY " + COLUMNS.MASTER_TITLE + " ASC"

    private val QUERY_GET_TIMES_BY_ID = "SELECT" +
            " * FROM " + TABLES.TIMES +
            " WHERE " + COLUMNS.TIMES_COURSE_ID + "=?"

    override fun onCreate(db: SQLiteDatabase) {
        // @formatter:off
        // create the COURSE MASTER table
        // ** stores the generic info about the courses by id
		db.execSQL(
				"CREATE TABLE " + TABLES.MASTER + " ( " +
				COLUMNS.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +

				COLUMNS.MASTER_SUBJECT + " TEXT, " +
				COLUMNS.MASTER_NUMBER + " INTEGER, " +
				COLUMNS.MASTER_SECTION + " INTEGER, " +

				COLUMNS.MASTER_TITLE + " TEXT, " +

				COLUMNS.MASTER_CREDIT_HOURS + " INTEGER, " +
				COLUMNS.MASTER_INSTRUCTOR + " TEXT)")

        // create COURSE TIMES table
        // ** stores the days and times by id
		db.execSQL(
				"CREATE TABLE " + TABLES.TIMES + " ( " +
				COLUMNS.ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
				COLUMNS.TIMES_COURSE_ID + " INTEGER NOT NULL, " +

				COLUMNS.TIMES_DAY + " TEXT, " +
				COLUMNS.TIMES_TIME_BEGIN + " TEXT, " +
				COLUMNS.TIMES_TIME_END + " TEXT, " +

				COLUMNS.TIMES_COURSE_BUILDING + " TEXT, " +
				COLUMNS.TIMES_COURSE_ROOM + " TEXT)")
		// @formatter:on
    }

    fun insertCourse(course: CourseObject) {
        // create ContentValues for master table from course
        val masterValues = ContentValues()
        masterValues.put(COLUMNS.ID, course.courseID)
        masterValues.put(COLUMNS.MASTER_SUBJECT, course.subject)
        masterValues.put(COLUMNS.MASTER_NUMBER, course.number)
        masterValues.put(COLUMNS.MASTER_SECTION, course.section)
        masterValues.put(COLUMNS.MASTER_TITLE, course.title)
        masterValues.put(COLUMNS.MASTER_CREDIT_HOURS, course.creditHours)
        masterValues.put(COLUMNS.MASTER_INSTRUCTOR, course.instructorName)

        // add the course to the database
        val result = readableDatabase.insert(TABLES.MASTER, null, masterValues)

        // if insert did not fail
        if (result != -1L) {
            // store the generated courseID
            course.courseID = getCourseIDFromRow(result)

            // add each time to the times table
            for ((day, time) in course.times) {
                // create ContentValues from time entry
                val timeValues = ContentValues()

                timeValues.put(COLUMNS.TIMES_COURSE_ID, course.courseID)

                timeValues.put(COLUMNS.TIMES_DAY, day.toString())
                timeValues.put(COLUMNS.TIMES_TIME_BEGIN, time.start.toString())
                timeValues.put(COLUMNS.TIMES_TIME_END, time.end.toString())

                timeValues.put(COLUMNS.TIMES_COURSE_BUILDING, time.building)
                timeValues.put(COLUMNS.TIMES_COURSE_ROOM, time.room)

                // add the time to the database
                readableDatabase.insert(TABLES.TIMES, null, timeValues)
            }
        } else {
            // the insert had an error
            Log.e(MAROON_TAG, "an error occurred adding course: " + course.toString())
        }
    }

    fun getCoursesAsList(dow: DayOfWeek = DayOfWeek.NONE): ArrayList<CourseObject> {
        // create a list of courses to return
        val returnCourses = ArrayList<CourseObject>()

        // set the cursor depending if the day of week is requested
        val cursor: Cursor =
                if (dow == DayOfWeek.NONE)
                    getCursorAllMaster()
                else
                    getCursorByDay(dow)

        // read each row defined in the cursor
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                // create a CourseObject to hold data
                val course = CourseObject()

                // get the data from each column
                course.courseID = getCursorInt(cursor, COLUMNS.ID)
                course.subject = getCursorString(cursor, COLUMNS.MASTER_SUBJECT)
                course.number = getCursorInt(cursor, COLUMNS.MASTER_NUMBER)
                course.section = getCursorInt(cursor, COLUMNS.MASTER_SECTION)
                course.title = getCursorString(cursor, COLUMNS.MASTER_TITLE)
                course.creditHours = getCursorInt(cursor, COLUMNS.MASTER_CREDIT_HOURS)
                course.instructorName = getCursorString(cursor, COLUMNS.MASTER_INSTRUCTOR)

                // get the course times
                course.times = getCourseTimes(course.courseID)

                // add the course to the list
                returnCourses.add(course)
            }
        }

        // close the cursor
        cursor.close()

        // return the list of courses
        return returnCourses
    }

    fun getCourseTimes(courseID: Int): HashMap<DayOfWeek, CourseTime> {
        // create a map to return
        val returnMap = HashMap<DayOfWeek, CourseTime>()

        // get a cursor of the current time
        val cursor = getCursorTimesByID(courseID)

        // define how the time should be formatted
        val parseFormat = DateTimeFormatterBuilder().appendPattern("h:mm:ss a").toFormatter()

        // read each row defined in the cursor
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                // get the data from each column
                val dayString = getCursorString(cursor, COLUMNS.TIMES_DAY)
                val startTime = getCursorString(cursor, COLUMNS.TIMES_TIME_BEGIN)
                val endTime = getCursorString(cursor, COLUMNS.TIMES_TIME_END)
                val building = getCursorString(cursor, COLUMNS.TIMES_COURSE_BUILDING)
                val room = getCursorString(cursor, COLUMNS.TIMES_COURSE_ROOM)

                // add the data to the map
                returnMap.put(
                        DayOfWeek.valueOf(dayString),
                        CourseTime(LocalTime.parse(startTime, parseFormat), LocalTime.parse(endTime, parseFormat), building, room)
                )
            }
        }

        // close the cursor
        cursor.close()

        // return the map
        return returnMap
    }

    /**
     * Queries the database for all columns from the MASTER table
     */
    fun getCursorAllMaster(): Cursor {
        return readableDatabase.rawQuery(QUERY_GET_ALL_MASTER, null)
    }

    /**
     * Queries the database for all columns joining MASTER and TIMES
     * based on master.id = times.course_id
     */
    fun getCursorByDay(dow: DayOfWeek): Cursor {
        return readableDatabase.rawQuery(QUERY_GET_BY_DAY, arrayOf(dow.toString()))
    }

    fun getCursorTimesByID(courseID: Int): Cursor {
        return readableDatabase.rawQuery(QUERY_GET_TIMES_BY_ID, arrayOf(courseID.toString()))
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        //TODO: Implement upgrades
    }

    private fun getCourseIDFromRow(rowID: Long): Int {
        // select the row defined by rowID
        val cursor = readableDatabase.rawQuery("SELECT rowid,? FROM ? WHERE rowid=?", arrayOf(COLUMNS.ID, TABLES.MASTER, rowID.toString()))

        // read each row defined in the cursor
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                // get the data
                val data = cursor.getInt(cursor.getColumnIndex(COLUMNS.ID))

                // close the cursor and return the data
                cursor.close()
                return data
            }
        }

        // the row wasn't found
        Log.w(MAROON_TAG, "row was not found: " + rowID)
        cursor.close()
        return -1
    }

    /**
     * Returns String data in column from cursor
     *
     * @param cursor the cursor to retrieve data from
     * @param column the column name to find
     * @return the string in the column
     */
    private fun getCursorString(cursor: Cursor, column: String): String {
        return cursor.getString(cursor.getColumnIndex(column))
    }

    /**
     * Returns Int data in column from cursor
     *
     * @param cursor the cursor to retrieve data from
     * @param column the column name to find
     * @return the integer in the column
     */
    private fun getCursorInt(cursor: Cursor, column: String): Int {
        return cursor.getInt(cursor.getColumnIndex(column))
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "courseDatabase.db"
    }
}
