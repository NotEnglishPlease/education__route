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
import androidx.lifecycle.ViewModelProvider

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
        get() {
            val filtered = if (_searchQuery.value.isBlank()) {
                _availableCourses
            } else {
                _availableCourses.filter { course ->
                    course.subject.contains(_searchQuery.value, ignoreCase = true)
                }
            }
            return filtered.sortedWith(
                compareBy<Course> { getDayOrder(it.weekDay) }
                    .thenBy { parseTime(it.time) }
            )
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

    fun enrollToCourse(clientId: Int, lessonId: Int, onSuccess: (() -> Unit)? = null, onError: ((String) -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.enrollToCourse(clientId, lessonId)
                if (response.isSuccessful) {
                    loadAvailableCourses(clientId)
                    loadMyCourses(clientId)
                    onSuccess?.invoke()
                } else {
                    val errorBody = response.errorBody()?.string()
                    if (errorBody?.contains("Already enrolled") == true) {
                        loadAvailableCourses(clientId)
                        loadMyCourses(clientId)
                        onError?.invoke("Вы уже записаны на этот курс")
                    } else {
                        onError?.invoke(errorBody ?: "Ошибка записи на курс")
                    }
                }
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Ошибка записи на курс")
            }
        }
    }

    fun loadMyCourses(clientId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = RetrofitInstance.api.getMyCourses(clientId)
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        val employees = RetrofitInstance.api.getEmployees()
                        _enrolledCourses.clear()
                        data.forEach { item ->
                            val lessonMap = item["lesson"] as? Map<*, *> ?: return@forEach
                            val visitMap = item["lessonVisit"] as? Map<*, *> ?: return@forEach
                            val employeeId = (lessonMap["employeeId"] as? Double)?.toInt() ?: 0
                            val teacher = employees.find { it.id == employeeId }?.name ?: "Преподаватель"
                            _enrolledCourses.add(
                                EnrolledCourse(
                                    id = (lessonMap["id"] as? Double)?.toInt() ?: 0,
                                    subject = lessonMap["subject"] as? String ?: "",
                                    weekDay = lessonMap["weekDay"] as? String ?: "",
                                    time = lessonMap["time"] as? String ?: "",
                                    ageGroup = "${lessonMap["ageLevel"]?.toString() ?: "-"} лет",
                                    teacher = teacher,
                                    currentTopic = lessonMap["topic"] as? String ?: "-",
                                    grade = visitMap["garde"]?.toString(),
                                    homework = lessonMap["homework"] as? String
                                )
                            )
                        }
                    }
                } else {
                    _error.value = response.errorBody()?.string() ?: "Ошибка загрузки моих курсов"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Ошибка загрузки моих курсов"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun unenrollFromCourse(clientId: Int, courseId: Int, onSuccess: (() -> Unit)? = null, onError: ((String) -> Unit)? = null) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.unenrollFromCourse(clientId, courseId)
                if (response.isSuccessful) {
                    loadMyCourses(clientId)
                    loadAvailableCourses(clientId)
                    onSuccess?.invoke()
                } else {
                    onError?.invoke(response.errorBody()?.string() ?: "Ошибка отписки от курса")
                }
            } catch (e: Exception) {
                onError?.invoke(e.message ?: "Ошибка отписки от курса")
            }
        }
    }

    fun removeCourse(courseId: Int) {
        _availableCourses.removeIf { it.id == courseId }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CourseViewModel() as T
            }
        }
    }
}