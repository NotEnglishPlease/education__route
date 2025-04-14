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
import com.example.educationroute.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController?) {
    val viewModel: LoginViewModel = viewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    // Переход на MainScreen при успешном входе
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController?.navigate("main") {
                popUpTo("login") { inclusive = true } // Удаляем экран входа из стека навигации
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
            Text(text = "Вход", style = MaterialTheme.typography.headlineMedium)

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

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { viewModel.login() }) {
                Text("Войти")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Нет аккаунта?")
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = {
                    navController?.navigate("register") {
                        popUpTo("login") { inclusive = true }
                    }
                }) {
                    Text("Регистрация")
                }
            }
        }

    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = null)
}