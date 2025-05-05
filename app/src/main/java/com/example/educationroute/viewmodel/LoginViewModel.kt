package com.example.educationroute.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _isTutor = MutableStateFlow(false)
    val isTutor: StateFlow<Boolean> = _isTutor.asStateFlow()

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun login(): Boolean {
        // Проверяем заполненность полей
        if (_email.value.isEmpty() || _password.value.isEmpty()) {
            _isError.value = true
            _errorMessage.value = "Пожалуйста, заполните все поля"
            return false
        }

        // Сбрасываем состояния перед проверкой
        _isError.value = false
        _errorMessage.value = ""
        _isTutor.value = false
        _isAdmin.value = false
        _isLoggedIn.value = false

        // Проверка на роль администратора
        if (_email.value == "admin@gmail.com" && _password.value == "12345678") {
            _isAdmin.value = true
            _isLoggedIn.value = true
            return true
        }

        // Проверка на роль преподавателя
        if (_email.value == "tutor@gmail.com" && _password.value == "12345678") {
            _isTutor.value = true
            _isLoggedIn.value = true
            return true
        }

        // Проверка на роль ученика
        if (_email.value == "student@gmail.com" && _password.value == "12345678") {
            _isLoggedIn.value = true
            return true
        }

        // Если ни одна проверка не прошла
        _isError.value = true
        _errorMessage.value = "Неверный email или пароль"
        return false
    }

    fun resetState() {
        _email.value = ""
        _password.value = ""
        _isError.value = false
        _errorMessage.value = ""
        _isTutor.value = false
        _isAdmin.value = false
        _isLoggedIn.value = false
    }
}

// Функция расширения для проверки email
private fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return matches(emailRegex)
}