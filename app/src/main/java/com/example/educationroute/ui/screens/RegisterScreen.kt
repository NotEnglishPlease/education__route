package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.educationroute.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(navController: NavController?) {
    val viewModel: RegisterViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isRegistered by viewModel.isRegistered.collectAsState()

    // Переход на MainScreen при успешной регистрации
    LaunchedEffect(isRegistered) {
        if (isRegistered) {
            navController?.navigate("main") {
                popUpTo("register") { inclusive = true } // Удаляем экран регистрации из стека навигации
            }
        }
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Регистрация", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Пароль") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Подтвердите пароль") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { viewModel.register() }) {
                Text("Зарегистрироваться")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Уже есть аккаунт?")
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = { navController?.navigate("login") {
                    popUpTo("register") { inclusive = true }
                } }) {
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