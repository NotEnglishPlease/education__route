package com.example.educationroute.data

data class EditLesson(
    val id: Int? = null,
    val subject: String,
    val ageGroup: String,
    val weekDay: String,
    val startTime: String,
    val endTime: String,
    val tutor: String,
    val students: List<Student>
)

data class Student(
    val id: Int,
    val name: String,
    val isSelected: Boolean
) 