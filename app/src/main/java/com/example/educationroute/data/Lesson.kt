package com.example.educationroute.data

data class Lesson(
    val courseId: Int,
    val topic: String,
    val homework: String,
    val studentGrades: Map<Int, StudentGrade> // ID студента -> оценка
)

data class StudentGrade(
    val grade: Int?, // null если отсутствовал
    val isPresent: Boolean
) 