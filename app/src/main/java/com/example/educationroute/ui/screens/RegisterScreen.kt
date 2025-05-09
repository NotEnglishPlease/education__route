package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.educationroute.viewmodel.RegisterViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun RegisterScreen(navController: NavController?) {
    val viewModel: RegisterViewModel = viewModel()
    val parentName by viewModel.parentName.collectAsState()
    val email by viewModel.email.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val password by viewModel.password.collectAsState()
    val childName by viewModel.childName.collectAsState()
    val childBirthday by viewModel.childBirthday.collectAsState()

    val isRegistered by viewModel.isRegistered.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(isRegistered) {
        if (isRegistered) {
            navController?.navigate("main") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .padding(top = 48.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Регистрация", style = MaterialTheme.typography.headlineMedium)

            // Данные родителя
            Text(text = "Данные родителя", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = parentName,
                onValueChange = {
                    viewModel.updateParentName(it)
                },
                label = { Text("ФИО родителя") },
                modifier = Modifier.fillMaxWidth()
            )

            // Данные ребенка
            Text(text = "Данные ребенка", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = childName,
                onValueChange = {
                    viewModel.updateChildName(it)
                },
                label = { Text("ФИО ребенка") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = childBirthday,
                onValueChange = {
                    viewModel.updateChildBirthday(it)
                },
                label = { Text("Дата рождения ребенка (ДД.ММ.ГГГГ)") },
                modifier = Modifier.fillMaxWidth()
            )

            // Контактные данные
            Text(text = "Контактные данные", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = email,
                onValueChange = {
                    viewModel.updateEmail(it)
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = phone,
                onValueChange = {
                    viewModel.updatePhone(it)
                },
                label = { Text("Телефон") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    viewModel.updatePassword(it)
                },
                label = { Text("Пароль") },
                modifier = Modifier.fillMaxWidth()
            )

            if (error != null) {
                Text(text = error ?: "", color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = {
                    viewModel.register()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Зарегистрироваться")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Уже есть аккаунт?")
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = { 
                    navController?.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }) {
                    Text("Войти")
                }
            }
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(navController = null)
}