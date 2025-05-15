package com.example.educationroute.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.educationroute.data.EditLesson
import androidx.compose.runtime.rememberCoroutineScope
import com.example.educationroute.model.LessonDTO
import com.example.educationroute.network.RetrofitInstance
import kotlinx.coroutines.launch
import com.example.educationroute.model.EmployeeDTO
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextFieldDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLessonScreen(navController: NavController, lesson: EditLesson) {
    var subject by remember { mutableStateOf(lesson.subject) }
    var ageGroup by remember { mutableStateOf(lesson.ageGroup) }
    var weekDay by remember { mutableStateOf(lesson.weekDay) }
    var startTime by remember { mutableStateOf(lesson.startTime) }
    var endTime by remember { mutableStateOf(lesson.endTime) }
    var tutor by remember { mutableStateOf(lesson.tutor) }

    val coroutineScope = rememberCoroutineScope()
    var employees by remember { mutableStateOf<List<EmployeeDTO>>(emptyList()) }
    var selectedEmployee by remember { mutableStateOf<EmployeeDTO?>(null) }

    var expanded by remember { mutableStateOf(false) }

    val filteredEmployees = if (subject.isNotBlank()) {
        employees.filter { it.profile?.equals(subject, ignoreCase = true) == true }
    } else {
        emptyList()
    }

    val weekDays = listOf(
        "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"
    )
    val subjects = listOf(
        "математика", "информатика", "русский язык", "английский язык", "китайский язык", "география", "биология", "химия", "физика", "обществознание", "история"
    )
    var weekDayExpanded by remember { mutableStateOf(false) }
    var subjectExpanded by remember { mutableStateOf(false) }

    var existingLessons by remember { mutableStateOf<List<LessonDTO>>(emptyList()) }

    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var deleteError by remember { mutableStateOf<String?>(null) }

    // Загрузка списка преподавателей
    LaunchedEffect(Unit) {
        try {
            employees = RetrofitInstance.api.getEmployees()
            // Если это редактирование, находим преподавателя по ID
            if (lesson.id != null) {
                selectedEmployee = employees.firstOrNull { it.id == lesson.id }
            }
            existingLessons = RetrofitInstance.api.getLessons()
        } catch (e: Exception) {
            println("Error loading employees: ${e.message}")
        }
    }

    // Обновляем выбранного преподавателя при изменении предмета
    LaunchedEffect(subject) {
        if (subject.isNotBlank()) {
            selectedEmployee = filteredEmployees.firstOrNull()
        }
    }

    // Функция для фильтрации ввода времени
    fun filterTimeInput(input: String): String {
        return input.filter { it.isDigit() }.take(4)
    }

    // Валидация для времени (hh:mm)
    fun isValidTime(input: String): Boolean {
        if (input.length != 4) return false
        val hours = input.take(2).toIntOrNull() ?: return false
        val minutes = input.drop(2).toIntOrNull() ?: return false
        return hours in 0..23 && minutes in 0..59
    }

    // Валидация для возраста
    fun isValidAge(input: String): Boolean {
        val age = input.toIntOrNull() ?: return false
        return age in 7..18
    }

    // Визуальная маска для времени
    val timeVisualTransformation = VisualTransformation { text ->
        val trimmed = text.text.take(5)
        val out = StringBuilder()
        for (i in trimmed.indices) {
            out.append(trimmed[i])
            if (i == 1 && trimmed.length > 2) out.append(":")
        }
        TransformedText(
            AnnotatedString(out.toString()),
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (offset <= 2) return offset
                    if (offset <= 4) return offset + 1
                    return out.length
                }
                override fun transformedToOriginal(offset: Int): Int {
                    if (offset <= 2) return offset
                    if (offset <= 5) return offset - 1
                    return trimmed.length
                }
            }
        )
    }

    // Форматирование времени в hh:mm
    fun formatTime(input: String): String {
        val digits = input.filter { it.isDigit() }
        if (digits.length < 4) return ""
        return "${digits.take(2)}:${digits.drop(2).take(2)}"
    }

    // Проверка времени на диапазон
    fun isTimeInRange(time: String, min: String, max: String): Boolean {
        if (!Regex("^\\d{2}:\\d{2}$").matches(time)) return false
        val parts = time.split(":")
        val hour = parts[0].toIntOrNull() ?: return false
        val minute = parts[1].toIntOrNull() ?: return false
        val minParts = min.split(":")
        val maxParts = max.split(":")
        val minHour = minParts.getOrNull(0)?.toIntOrNull() ?: return false
        val minMinute = minParts.getOrNull(1)?.toIntOrNull() ?: return false
        val maxHour = maxParts.getOrNull(0)?.toIntOrNull() ?: return false
        val maxMinute = maxParts.getOrNull(1)?.toIntOrNull() ?: return false
        val total = hour * 60 + minute
        val minTotal = minHour * 60 + minMinute
        val maxTotal = maxHour * 60 + maxMinute
        return total in minTotal..maxTotal
    }

    // Проверка длительности занятия (1 или 2 часа)
    fun isValidDuration(start: String, end: String): Boolean {
        if (!isValidTime(start) || !isValidTime(end)) return false
        
        val startHours = start.take(2).toInt()
        val startMinutes = start.drop(2).toInt()
        val endHours = end.take(2).toInt()
        val endMinutes = end.drop(2).toInt()
        
        val startTotal = startHours * 60 + startMinutes
        val endTotal = endHours * 60 + endMinutes
        
        val duration = endTotal - startTotal
        return duration == 60 || duration == 120 // 1 час или 2 часа
    }

    var timeError by remember { mutableStateOf<String?>(null) }
    var validationError by remember { mutableStateOf<String?>(null) }

    // Преобразование времени в минуты
    fun timeToMinutes(time: String): Int {
        val hours = time.take(2).toIntOrNull() ?: return -1
        val minutes = time.drop(2).toIntOrNull() ?: return -1
        return hours * 60 + minutes
    }

    fun hasTimeConflict(start: String, end: String, weekDay: String): Boolean {
        val startMinutes = timeToMinutes(start)
        val endMinutes = timeToMinutes(end)
        
        if (startMinutes == -1 || endMinutes == -1) return false

        val startWithBreak = startMinutes - 10
        val endWithBreak = endMinutes + 10

        return existingLessons
            .filter { it.weekDay == weekDay && it.id != lesson.id }
            .any { existingLesson ->
                val existingTime = existingLesson.time.split("-")
                if (existingTime.size != 2) return@any false
                
                val existingStart = timeToMinutes(existingTime[0].replace(":", ""))
                val existingEnd = timeToMinutes(existingTime[1].replace(":", ""))
                
                if (existingStart == -1 || existingEnd == -1) return@any false

                (startWithBreak < existingEnd && endWithBreak > existingStart)
            }
    }

    // Проверка заполнения всех обязательных полей
    fun validateFields(): Boolean {
        when {
            subject.isBlank() -> {
                validationError = "Выберите предмет"
                return false
            }
            ageGroup.isBlank() -> {
                validationError = "Введите возрастную группу"
                return false
            }
            !isValidAge(ageGroup) -> {
                validationError = "Возрастная группа должна быть от 7 до 18 лет"
                return false
            }
            weekDay.isBlank() -> {
                validationError = "Выберите день недели"
                return false
            }
            startTime.isBlank() -> {
                validationError = "Введите время начала"
                return false
            }
            endTime.isBlank() -> {
                validationError = "Введите время окончания"
                return false
            }
            selectedEmployee == null -> {
                validationError = "Выберите преподавателя"
                return false
            }
            !isValidTime(startTime) -> {
                timeError = "Введите корректное время начала в формате чч:мм"
                return false
            }
            !isValidTime(endTime) -> {
                timeError = "Введите корректное время окончания в формате чч:мм"
                return false
            }
            !isTimeInRange(formatTime(startTime), "15:00", "20:00") -> {
                timeError = "Время начала не раньше 15:00 и не позже 20:00"
                return false
            }
            !isTimeInRange(formatTime(endTime), "16:00", "21:00") -> {
                timeError = "Время окончания не раньше 16:00 и не позже 21:00"
                return false
            }
            !isValidDuration(startTime, endTime) -> {
                timeError = "Занятие должно длиться 1 или 2 часа"
                return false
            }
            hasTimeConflict(startTime, endTime, weekDay) -> {
                timeError = "Время занятия пересекается с другим занятием или не соблюден перерыв в 10 минут"
                return false
            }
            else -> {
                validationError = null
                timeError = null
                return true
            }
        }
    }

    if (showDeleteConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmationDialog = false },
            title = { Text("Подтверждение удаления") },
            text = { Text("Вы уверены, что хотите удалить это занятие?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                val response = RetrofitInstance.api.deleteLesson(lesson.id ?: 0)
                                if (response.isSuccessful) {
                                    navController.popBackStack()
                                } else {
                                    deleteError = "Ошибка при удалении: ${response.code()}"
                                }
                            } catch (e: Exception) {
                                deleteError = "Ошибка при удалении: ${e.message}"
                            }
                            showDeleteConfirmationDialog = false
                        }
                    }
                ) {
                    Text("Удалить", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmationDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    if (deleteError != null) {
        AlertDialog(
            onDismissRequest = { deleteError = null },
            title = { Text("Ошибка") },
            text = { Text(deleteError!!) },
            confirmButton = {
                TextButton(onClick = { deleteError = null }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактор занятия") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (!validateFields()) return@TextButton
                            coroutineScope.launch {
                                try {
                                    val timeString = "${formatTime(startTime)}-${formatTime(endTime)}"
                                    println("Sending time: $timeString")
                                    val dto = LessonDTO(
                                        id = lesson.id ?: 0,
                                        employeeId = selectedEmployee?.id ?: 1,
                                        subject = subject,
                                        topic = null,
                                        time = timeString,
                                        weekDay = weekDay,
                                        ageLevel = ageGroup.toIntOrNull() ?: 0,
                                        homework = null
                                    )
                                    if (lesson.id == null) {
                                        // Создание нового занятия
                                        RetrofitInstance.api.createLesson(dto)
                                    } else {
                                        // Редактирование существующего занятия
                                        val response = RetrofitInstance.api.updateLesson(lesson.id, dto)
                                        if (!response.isSuccessful) {
                                            validationError = "Ошибка при обновлении: ${response.code()}"
                                            return@launch
                                        }
                                    }
                                    navController.popBackStack()
                                } catch (e: Exception) {
                                    validationError = "Ошибка при сохранении: ${e.message}"
                                }
                            }
                        },
                        enabled = validateFields()
                    ) {
                        Text("Сохранить")
                    }
                }
            )
        },
        floatingActionButton = {
            if (lesson.id != null) {
                FloatingActionButton(
                    onClick = { showDeleteConfirmationDialog = true },
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Удалить занятие")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ПРЕДМЕТ
            item {
                Text(
                    "Предмет",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = subjectExpanded,
                    onExpandedChange = { subjectExpanded = it }
                ) {
                    OutlinedTextField(
                        value = subject,
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = { Text("Предмет") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(subjectExpanded)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = subjectExpanded,
                        onDismissRequest = { subjectExpanded = false }
                    ) {
                        subjects.forEach { subj ->
                            DropdownMenuItem(
                                text = { Text(subj.replaceFirstChar { it.uppercase() }) },
                                onClick = {
                                    subject = subj.replaceFirstChar { it.uppercase() }
                                    selectedEmployee = null
                                    subjectExpanded = false
                                }
                            )
                        }
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
            // ВОЗРАСТНАЯ ГРУППА
            item {
                Text(
                    "Возрастная группа",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = ageGroup,
                    onValueChange = { 
                        if (it.isEmpty() || (it.all { ch -> ch.isDigit() } && it.toIntOrNull() ?: 0 <= 18)) {
                            ageGroup = it
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Возрастная группа (от 7 до 18 лет)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
            // ДЕНЬ НЕДЕЛИ
            item {
                Text(
                    "День недели",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = weekDayExpanded,
                    onExpandedChange = { weekDayExpanded = it }
                ) {
                    OutlinedTextField(
                        value = weekDay,
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = { Text("День недели") },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(weekDayExpanded)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = weekDayExpanded,
                        onDismissRequest = { weekDayExpanded = false }
                    ) {
                        weekDays.forEach { day ->
                            DropdownMenuItem(
                                text = { Text(day) },
                                onClick = {
                                    weekDay = day
                                    weekDayExpanded = false
                                }
                            )
                        }
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
            // ВРЕМЯ
            item {
                Text(
                    "Расписание",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { 
                            val filtered = filterTimeInput(it)
                            if (filtered.length <= 4) {
                                startTime = filtered
                                timeError = null
                            }
                        },
                        modifier = Modifier.weight(1f),
                        label = { Text("Начало (чч:мм)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = timeVisualTransformation
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { 
                            val filtered = filterTimeInput(it)
                            if (filtered.length <= 4) {
                                endTime = filtered
                                timeError = null
                            }
                        },
                        modifier = Modifier.weight(1f),
                        label = { Text("Конец (чч:мм)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        visualTransformation = timeVisualTransformation
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
            // Показываем ошибку валидации, если есть
            if (validationError != null) {
                item {
                    Text(
                        text = validationError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }
            // Показываем ошибку времени, если есть
            if (timeError != null) {
                item {
                    Text(
                        text = timeError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }
            // ПРЕПОДАВАТЕЛЬ
            item {
                Text(
                    "Преподаватель",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { if (subject.isNotBlank()) expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedEmployee?.name ?: "",
                        onValueChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        label = { Text("ФИО преподавателя") },
                        readOnly = true,
                        enabled = subject.isNotBlank(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        filteredEmployees.forEach { employee ->
                            DropdownMenuItem(
                                text = { Text(employee.name) },
                                onClick = {
                                    selectedEmployee = employee
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
} 