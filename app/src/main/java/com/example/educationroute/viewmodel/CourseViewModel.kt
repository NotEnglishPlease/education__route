package com.example.educationroute.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationroute.data.Course
import com.example.educationroute.data.EnrolledCourse
import com.example.educationroute.network.RetrofitInstance
import com.example.educationroute.model.LessonDTO
import kotlinx.coroutines.launch

class CourseViewModel : ViewModel() {
    private val _availableCourses = mutableStateListOf<Course>()
    val availableCourses: List<Course> = _availableCourses

    private val _enrolledCourses = mutableStateListOf<EnrolledCourse>()
    val enrolledCourses: List<EnrolledCourse> = _enrolledCourses

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading.value

    private val _error = mutableStateOf<String?>(null)
    val error: String?
        get() = _error.value

    private val _searchQuery = mutableStateOf("")
    val searchQuery: String
        get() = _searchQuery.value

    private fun getDayOrder(day: String): Int {
        return when (day.lowercase()) {
            "понедельник" -> 1
            "вторник" -> 2
            "среда" -> 3
            "четверг" -> 4
            "пятница" -> 5
            "суббота" -> 6
            "воскресенье" -> 7
            else -> 8
        }
    }

    private fun parseTime(time: String): Int {
        return try {
            val parts = time.split(":")
            parts[0].toInt() * 60 + parts[1].toInt()
        } catch (e: Exception) {
            0
        }
    }

    val filteredCourses: List<Course>
        get() = if (_searchQuery.value.isBlank()) {
            _availableCourses
        } else {
            _availableCourses.filter { course ->
                course.subject.contains(_searchQuery.value, ignoreCase = true)
            }
        }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun loadAvailableCourses(clientId: Int) {
        Log.d("CourseViewModel", "Начало загрузки курсов для клиента $clientId")
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                Log.d("CourseViewModel", "Отправка запроса на получение доступных уроков")
                val response = RetrofitInstance.api.getAvailableLessons(clientId)
                Log.d("CourseViewModel", "Получен ответ: ${response.code()}")
                
                if (response.isSuccessful) {
                    val lessons = response.body()
                    Log.d("CourseViewModel", "Получено уроков: ${lessons?.size ?: 0}")
                    
                    if (lessons.isNullOrEmpty()) {
                        _error.value = "Нет доступных курсов"
                        return@launch
                    }
                    
                    Log.d("CourseViewModel", "Загрузка списка преподавателей")
                    val employees = RetrofitInstance.api.getEmployees()
                    Log.d("CourseViewModel", "Получено преподавателей: ${employees.size}")

                    _availableCourses.clear()
                    lessons.forEach { lesson ->
                        val teacher = employees.find { it.id == lesson.employeeId }
                        Log.d("CourseViewModel", "Обработка урока: ${lesson.subject}, преподаватель: ${teacher?.name}")
                        
                        _availableCourses.add(
                            Course(
                                id = lesson.id ?: 0,
                                subject = lesson.subject,
                                weekDay = lesson.weekDay,
                                time = lesson.time,
                                ageGroup = "${lesson.ageLevel} лет",
                                teacher = teacher?.name ?: "Преподаватель",
                                studentsCount = 0
                            )
                        )
                    }
                    Log.d("CourseViewModel", "Загружено курсов: ${_availableCourses.size}")
                } else {
                    val errorMessage = "Ошибка загрузки курсов: ${response.code()}"
                    Log.e("CourseViewModel", errorMessage)
                    _error.value = errorMessage
                }
            } catch (e: Exception) {
                val errorMessage = "Ошибка загрузки курсов: ${e.message}"
                Log.e("CourseViewModel", errorMessage, e)
                _error.value = errorMessage
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun enrollToCourse(courseId: Int) {
        // Логика записи на курс
        val course = _availableCourses.find { it.id == courseId }
        course?.let {
            _enrolledCourses.add(EnrolledCourse(
                id = it.id,
                subject = it.subject,
                weekDay = it.weekDay,
                time = it.time,
                ageGroup = it.ageGroup,
                teacher = it.teacher,
                currentTopic = "Новая тема",
                grade = null,
                homework = null
            ))
        }
    }

    fun unenrollFromCourse(courseId: Int) {
        // Логика отписки от курса
        _enrolledCourses.removeIf { it.id == courseId }
    }

    fun removeCourse(courseId: Int) {
        _availableCourses.removeIf { it.id == courseId }
    }
}