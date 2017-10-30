package com.operontech.maroon.db

data class CourseObject(
        var courseID: Int = 0,
        var subject: String? = "",
        var number: Int = 0,
        var section: Int = 0,
        var title: String? = "",
        var creditHours: Int = 0,
        var times: HashMap<DayOfWeek, CourseTime> = HashMap(),
        var instructorName: String? = "") {

    fun getTime(dow: DayOfWeek): CourseTime? = times[dow]

    fun setTime(dow: DayOfWeek, t: CourseTime) = times.put(dow, t)

    fun clearTimes() = times.clear()
}
