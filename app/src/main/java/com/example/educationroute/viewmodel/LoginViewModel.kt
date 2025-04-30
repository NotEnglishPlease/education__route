package com.example.educationroute.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError

    fun login(email: String, password: String) {
        viewModelScope.launch {
            // Временная проверка - можно заменить на реальный API-вызов
            if (email.isValidEmail() && password.length >= 8) {
                _isLoggedIn.value = true
                _loginError.value = null
            } else {
                _loginError.value = "Некорректные данные для входа"
            }
        }
    }
}

// Функция расширения для проверки email
private fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return matches(emailRegex)
}