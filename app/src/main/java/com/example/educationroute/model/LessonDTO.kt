package com.example.educationroute.model

data class LessonDTO(
    val id: Int,
    val employeeId: Int,
    val subject: String,
    val topic: String?,
    val time: String,
    val weekDay: String,
    val ageLevel: Int,
    val homework: String?
) 