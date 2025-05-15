package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.educationroute.model.ClientDTO
import com.example.educationroute.network.RetrofitInstance
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminClientsScreen(navController: NavController) {
    var clients by remember { mutableStateOf<List<ClientDTO>>(emptyList()) }
    val scope = rememberCoroutineScope()
    
    // Загрузка клиентов при первом запуске
    LaunchedEffect(Unit) {
        try {
            clients = RetrofitInstance.api.getClients().sortedBy { it.childName }
        } catch (e: Exception) {
            // TODO: Обработка ошибки
        }
    }
    
    var selectedClient by remember { mutableStateOf<ClientDTO?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var newPaidLessons by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Список клиентов") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = paddingValues.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(clients) { client ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Ученик: ${client.childName}",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Родитель: ${client.parentName}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Возраст: ${client.age} лет",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Телефон: ${client.phone}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Оплаченных занятий: ${client.paidLessons ?: 0}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                selectedClient = client
                                newPaidLessons = (client.paidLessons ?: 0).toString()
                                showDialog = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Изменить количество занятий")
                        }
                    }
                }
            }
        }
    }

    if (showDialog && selectedClient != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Изменить количество занятий") },
            text = {
                Column {
                    Text("Ученик: ${selectedClient?.childName}")
                    Text("Текущее количество: ${selectedClient?.paidLessons ?: 0}")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = newPaidLessons,
                        onValueChange = { newPaidLessons = it },
                        label = { Text("Новое количество занятий") },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                val newValue = newPaidLessons.toIntOrNull()
                                if (newValue != null && newValue >= 0) {
                                    selectedClient?.let { client ->
                                        RetrofitInstance.api.updatePaidLessons(client.id, newValue)
                                        // Обновляем список клиентов с сохранением сортировки
                                        clients = RetrofitInstance.api.getClients().sortedBy { it.childName }
                                    }
                                }
                                showDialog = false
                            } catch (e: Exception) {
                                // TODO: Показать ошибку пользователю
                            }
                        }
                    }
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
} 