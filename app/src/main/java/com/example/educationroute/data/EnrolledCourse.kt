package com.example.educationroute.data

data class EnrolledCourse(
    val id: Int,
    val subject: String,
    val weekDay: String,
    val time: String,
    val ageGroup: String,
    val teacher: String,
    val currentTopic: String,
    val grade: String?,
    val homework: String?
)