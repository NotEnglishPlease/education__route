package com.example.educationroute.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val loginError by viewModel.loginError.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    // Переход при успешном входе
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController?.navigate("main") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    fun validateInputs(): Boolean {
        emailError = when {
            email.isBlank() -> "Введите email"
            !email.isValidEmail() -> "Некорректный формат email"
            else -> null
        }

        passwordError = when {
            password.isBlank() -> "Введите пароль"
            password.length < 8 -> "Пароль должен содержать минимум 8 символов"
            else -> null
        }

        return emailError == null && passwordError == null
    }

    fun handleLogin() {
        if (validateInputs()) {
            viewModel.login(email, password)
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
                onValueChange = {
                    email = it
                    emailError = null
                },
                label = { Text("Email") },
                isError = emailError != null,
                supportingText = {
                    emailError?.let { error ->
                        Text(text = error)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null
                },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError != null,
                supportingText = {
                    passwordError?.let { error ->
                        Text(text = error)
                    }
                }
            )

            // Общая ошибка авторизации
            loginError?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { handleLogin() }
            ) {
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

// Функция расширения для Compose
private fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return matches(emailRegex)
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = null)
}