package com.example.educationroute.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.educationroute.data.Course
import com.example.educationroute.data.EnrolledCourse

class CourseViewModel : ViewModel() {
    private val _availableCourses = mutableStateOf<List<Course>>(emptyList())
    val availableCourses: List<Course> get() = _availableCourses.value

    private val _enrolledCourses = mutableStateOf<List<EnrolledCourse>>(emptyList())
    val enrolledCourses: List<EnrolledCourse> get() = _enrolledCourses.value

    init {
        loadData()
    }

    private fun loadData() {
        _availableCourses.value = listOf(
            Course(1, "Математика", "Понедельник", "15:00-16:30", "10-11 лет", "Иванова А.П.", 10),
            Course(2, "Физика", "Среда", "16:00-17:30", "12-14 лет", "Петров И.И.", 12)
        )

        _enrolledCourses.value = listOf(
            EnrolledCourse(
                1, "Математика", "Понедельник", "15:00-16:30", "10-11 лет", "Иванова А.П.",
                "Алгебра: квадратные уравнения", "5", "Упр. 123-125"
            )
        )
    }

    fun enrollToCourse(courseId: Int) {
        // Логика записи на курс
        val course = _availableCourses.value.find { it.id == courseId }
        course?.let {
            _enrolledCourses.value += EnrolledCourse(
                id = it.id,
                subject = it.subject,
                weekDay = it.weekDay,
                time = it.time,
                ageGroup = it.ageGroup,
                teacher = it.teacher,
                currentTopic = "Новая тема",
                grade = null,
                homework = null
            )
        }
    }

    fun unenrollFromCourse(courseId: Int) {
        // Логика отписки от курса
        _enrolledCourses.value = _enrolledCourses.value.filter { it.id != courseId }
    }

    fun removeCourse(courseId: Int) {
        _availableCourses.value = _availableCourses.value.filter { it.id != courseId }
    }
}