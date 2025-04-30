package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(navController: NavController) {
    val subjects = listOf(
        "Информатика", "Математика", "Русский язык", "Английский язык",
        "Физика", "История", "Обществознание", "Биология", "Химия", "География"
    )
    val weekDays = listOf(
        "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"
    )

    var selectedSubject by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var selectedDays by remember { mutableStateOf(setOf<String>()) }
    var startTime by remember { mutableStateOf(LocalTime.of(15, 0)) }
    var endTime by remember { mutableStateOf(LocalTime.of(16, 30)) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    var showAgeError by remember { mutableStateOf(false) }
    var showTimeError by remember { mutableStateOf(false) }

    val startTimePickerState = rememberTimePickerState(
        initialHour = startTime.hour,
        initialMinute = startTime.minute,
        is24Hour = true
    )

    val endTimePickerState = rememberTimePickerState(
        initialHour = endTime.hour,
        initialMinute = endTime.minute,
        is24Hour = true
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Фильтры") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {
                        val ageInt = age.toIntOrNull()
                        showAgeError = ageInt == null || ageInt !in 7..17
                        showTimeError = startTime.isBefore(LocalTime.of(15, 0)) ||
                                endTime != startTime.plusMinutes(90)

                        if (!showAgeError && !showTimeError) {
                            navController.popBackStack()
                        }
                    }) {
                        Text("Применить")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Предмет", style = MaterialTheme.typography.titleMedium)
                Column {
                    subjects.forEach { subject ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = subject == selectedSubject,
                                    onClick = { selectedSubject = subject }
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = subject == selectedSubject,
                                onClick = { selectedSubject = subject }
                            )
                            Text(
                                text = subject,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Text("Возраст", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = age,
                    onValueChange = { newValue -> age = newValue },
                    label = { Text("От 7 до 17 лет") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = showAgeError
                )
                if (showAgeError) {
                    Text(
                        text = "Возраст должен быть от 7 до 17 лет",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Text("День недели", style = MaterialTheme.typography.titleMedium)
                Column {
                    weekDays.forEach { day ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = day in selectedDays,
                                onCheckedChange = { checked ->
                                    selectedDays = if (checked) {
                                        selectedDays + day
                                    } else {
                                        selectedDays - day
                                    }
                                }
                            )
                            Text(
                                text = day,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Text("Время начала", style = MaterialTheme.typography.titleMedium)
                Button(
                    onClick = { showStartTimePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("${startTime.hour.toString().padStart(2, '0')}:${startTime.minute.toString().padStart(2, '0')}")
                }

                if (showStartTimePicker) {
                    AlertDialog(
                        onDismissRequest = { showStartTimePicker = false },
                        title = { Text("Выберите время начала") },
                        text = {
                            TimePicker(
                                state = startTimePickerState,
                                colors = TimePickerDefaults.colors()
                            )
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    startTime = LocalTime.of(
                                        startTimePickerState.hour,
                                        startTimePickerState.minute
                                    )
                                    showStartTimePicker = false
                                }
                            ) {
                                Text("ОК")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showStartTimePicker = false }) {
                                Text("Отмена")
                            }
                        }
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                Text("Время окончания", style = MaterialTheme.typography.titleMedium)
                Button(
                    onClick = { showEndTimePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("${endTime.hour.toString().padStart(2, '0')}:${endTime.minute.toString().padStart(2, '0')}")
                }

                if (showEndTimePicker) {
                    AlertDialog(
                        onDismissRequest = { showEndTimePicker = false },
                        title = { Text("Выберите время окончания") },
                        text = {
                            TimePicker(
                                state = endTimePickerState,
                                colors = TimePickerDefaults.colors()
                            )
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    endTime = LocalTime.of(
                                        endTimePickerState.hour,
                                        endTimePickerState.minute
                                    )
                                    showEndTimePicker = false
                                }
                            ) {
                                Text("ОК")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showEndTimePicker = false }) {
                                Text("Отмена")
                            }
                        }
                    )
                }

                if (showTimeError) {
                    Text(
                        text = "Занятие должно начинаться не ранее 15:00 и длиться 1.5 часа",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
