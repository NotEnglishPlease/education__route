package com.example.educationroute.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationroute.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    private val _isLoginSuccessful = MutableStateFlow(false)
    val isLoginSuccessful: StateFlow<Boolean> = _isLoginSuccessful.asStateFlow()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun login() {
        // Проверяем заполненность полей
        if (_email.value.isEmpty() || _password.value.isEmpty()) {
            _isError.value = true
            _errorMessage.value = "Пожалуйста, заполните все поля"
            return
        }

        // Проверяем валидность email
        if (!_email.value.isValidEmail()) {
            _isError.value = true
            _errorMessage.value = "Неверный формат email"
            return
        }

        // Сбрасываем состояния перед проверкой
        _isError.value = false
        _errorMessage.value = ""
        _isTutor.value = false
        _isAdmin.value = false
        _isLoginSuccessful.value = false

        viewModelScope.launch {
            Log.d("DEBUG_DEBUG", "login attempt with email: ${_email.value}")
            try {
                val response = RetrofitInstance.api.login(
                    email = _email.value,
                    password = _password.value
                )
                Log.d("DEBUG_DEBUG", "response code: ${response.code()}")
                Log.d("DEBUG_DEBUG", "response message: ${response.message()}")

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("DEBUG_DEBUG", "login response: $loginResponse")
                    if (loginResponse?.success == true) {
                        when (loginResponse.role) {
                            "admin" -> _isAdmin.value = true
                            "tutor" -> _isTutor.value = true
                        }
                        _isLoginSuccessful.value = true
                        _errorMessage.value = ""
                    } else {
                        _isError.value = true
                        _errorMessage.value = loginResponse?.message ?: "Ошибка входа"
                    }
                } else {
                    _isError.value = true
                    val error = response.errorBody()?.string()
                    Log.d("DEBUG_DEBUG", "login error body: $error")
                    _errorMessage.value = error ?: "Ошибка сервера"
                }
            } catch (e: Exception) {
                _isError.value = true
                Log.e("DEBUG_DEBUG", "login exception", e)
                _errorMessage.value = "Ошибка подключения к серверу: ${e.message}"
            }
        }
    }

    fun resetState() {
        _email.value = ""
        _password.value = ""
        _isError.value = false
        _errorMessage.value = ""
        _isTutor.value = false
        _isAdmin.value = false
        _isLoginSuccessful.value = false
    }
}

// Функция расширения для проверки email
private fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return matches(emailRegex)
}