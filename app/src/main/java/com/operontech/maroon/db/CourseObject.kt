package com.operontech.maroon.db

data class CourseObject(
        val subject: String? = "",
        val number: Int = 0,
        val section: Int = 0,
        val title: String? = "",
        val creditHours: Int = 0,
        val times: HashMap<DayOfWeek, CourseTime> = HashMap(),
        val building: String? = "",
        val roomNumber: String? = "",
        val instructorName: String? = "") {

    fun getTime(dow: DayOfWeek): CourseTime? = times[dow]

    fun setTime(dow: DayOfWeek, t: CourseTime) = times.put(dow, t)

    fun clearTimes() = times.clear()
}
