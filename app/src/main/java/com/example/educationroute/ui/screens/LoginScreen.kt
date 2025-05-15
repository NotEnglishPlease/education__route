package com.example.educationroute.ui.screens

import android.util.Log
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
import androidx.navigation.compose.rememberNavController
import com.example.educationroute.viewmodel.LoginViewModel
import com.example.educationroute.viewmodel.MainViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val loginViewModel: LoginViewModel = viewModel()
    val mainViewModel: MainViewModel = viewModel(
        key = "main_view_model",
        factory = MainViewModel.Factory
    )
    val email by loginViewModel.email.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val isError by loginViewModel.isError.collectAsState()
    val errorMessage by loginViewModel.errorMessage.collectAsState()
    val isTutor by loginViewModel.isTutor.collectAsState()
    val isAdmin by loginViewModel.isAdmin.collectAsState()
    val isLoginSuccessful by loginViewModel.isLoginSuccessful.collectAsState()
    val clientId by loginViewModel.clientId.collectAsState()

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(isLoginSuccessful) {
        if (isLoginSuccessful) {
            Log.d("LoginScreen", "Вход выполнен успешно")
            Log.d("LoginScreen", "clientId: $clientId")
            clientId?.let { id ->
                Log.d("LoginScreen", "Перенаправление на главный экран с clientId: $id")
            when {
                    isAdmin -> {
                        Log.d("LoginScreen", "Перенаправление на админ-панель")
                        navController.navigate("admin_main")
                    }
                    isTutor -> {
                        Log.d("LoginScreen", "Перенаправление на панель преподавателя")
                        navController.navigate("tutor_courses")
                    }
                    else -> {
                        Log.d("LoginScreen", "Перенаправление на главный экран")
                        mainViewModel.setClientId(id)
                        Log.d("LoginScreen", "clientId установлен в MainViewModel: $id")
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            } ?: run {
                Log.e("LoginScreen", "clientId равен null")
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
            Text(
                text = "Вход",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { loginViewModel.setEmail(it) },
                label = { Text("Email") },
                isError = isError,
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { loginViewModel.setPassword(it) },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                isError = isError,
                enabled = !isLoading
            )

            if (isError) {
                Text(
                    text = errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isLoading = true
                    loginViewModel.login()
                    isLoading = false
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Войти")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Нет аккаунта?")
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = {
                        navController.navigate("register")
                    }
                ) {
                    Text("Зарегистрироваться")
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

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen(navController = rememberNavController())
    }
}