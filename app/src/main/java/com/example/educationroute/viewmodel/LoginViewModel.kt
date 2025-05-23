package com.example.educationroute.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationroute.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoginSuccessful = MutableStateFlow(false)
    val isLoginSuccessful: StateFlow<Boolean> = _isLoginSuccessful

    private val _isTutor = MutableStateFlow(false)
    val isTutor: StateFlow<Boolean> = _isTutor

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin

    private val _clientId = MutableStateFlow<Int?>(null)
    val clientId: StateFlow<Int?> = _clientId

    fun setEmail(value: String) {
        _email.value = value
    }

    fun setPassword(value: String) {
        _password.value = value
    }

    fun setError(message: String) {
        _errorMessage.value = message
        _isError.value = true
    }

    fun login() {
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Начало входа в систему")
                _isError.value = false
                _errorMessage.value = null
                _isLoginSuccessful.value = false
                _isTutor.value = false
                _isAdmin.value = false
                _clientId.value = null

                // Локальная проверка для администратора и преподавателя
                when {
                    _email.value == "admin@gmail.com" && _password.value == "12345678" -> {
                        Log.d("LoginViewModel", "Локальный вход администратора")
                        _isAdmin.value = true
                        _isLoginSuccessful.value = true
                        return@launch
                    }
                    _email.value == "tutor@gmail.com" && _password.value == "12345678" -> {
                        Log.d("LoginViewModel", "Локальный вход преподавателя")
                        _isTutor.value = true
                        _isLoginSuccessful.value = true
                        return@launch
                    }
                }

                val response = RetrofitInstance.api.login(_email.value, _password.value)
                Log.d("LoginViewModel", "Получен ответ от сервера: ${response.code()}")
                
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("LoginViewModel", "Ответ сервера: $loginResponse")
                    
                    if (loginResponse?.success == true) {
                        when (loginResponse.role) {
                            "admin" -> {
                                Log.d("LoginViewModel", "Установка роли: admin")
                                _isAdmin.value = true
                            }
                            "tutor" -> {
                                Log.d("LoginViewModel", "Установка роли: tutor")
                                _isTutor.value = true
                            }
                            "client" -> {
                                Log.d("LoginViewModel", "Установка роли: client")
                                loginResponse.client?.let { client ->
                                    Log.d("LoginViewModel", "Установка clientId: ${client.id}")
                                    _clientId.value = client.id
                                }
                            }
                        }
                        Log.d("LoginViewModel", "Вход выполнен успешно")
                        _isLoginSuccessful.value = true
                    } else {
                        Log.e("LoginViewModel", "Ошибка входа: ${loginResponse?.message}")
                        _errorMessage.value = loginResponse?.message ?: "Ошибка входа"
                        _isError.value = true
                    }
                } else {
                    Log.e("LoginViewModel", "Ошибка сервера: ${response.code()}")
                    _errorMessage.value = "Ошибка сервера: ${response.code()}"
                    _isError.value = true
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Исключение при входе", e)
                _errorMessage.value = "Ошибка: ${e.message}"
                _isError.value = true
            }
        }
    }

    fun resetState() {
        _isError.value = false
        _errorMessage.value = null
        _isLoginSuccessful.value = false
        _isTutor.value = false
        _isAdmin.value = false
        _clientId.value = null
    }
}

// Функция расширения для проверки email
private fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return matches(emailRegex)
}