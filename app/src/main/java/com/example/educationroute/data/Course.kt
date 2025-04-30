package com.example.educationroute.data

data class Course(
    val id: Int,
    val subject: String,          // Название предмета
    val weekDay: String,         // День недели (например, "Понедельник")
    val time: String,            // Время (например, "15:00-16:30")
    val ageGroup: String,
    val teacher: String,         // ФИО преподавателя (например, "Иванова А.П.")
    val studentsCount: Int       // Количество учеников в группе
)