package com.example.educationroute.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.educationroute.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _parentName = MutableStateFlow("Иванов Иван Иванович")
    val parentName: StateFlow<String> = _parentName

    private val _email = MutableStateFlow("test@test.com")
    val email: StateFlow<String> = _email

    private val _phone = MutableStateFlow("+71231231212")
    val phone: StateFlow<String> = _phone

    private val _password = MutableStateFlow("1234")
    val password: StateFlow<String> = _password

    private val _childName = MutableStateFlow("Иванов Иван Иванович")
    val childName: StateFlow<String> = _childName

    private val _childBirthday = MutableStateFlow("10.10.2018")
    val childBirthday: StateFlow<String> = _childBirthday

    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun updateParentName(newName: String) { _parentName.value = newName }
    fun updateEmail(newEmail: String) { _email.value = newEmail }
    fun updatePhone(newPhone: String) { _phone.value = newPhone }
    fun updatePassword(newPassword: String) { _password.value = newPassword }
    fun updateChildName(newName: String) { _childName.value = newName }
    fun updateChildBirthday(newBirthday: String) { _childBirthday.value = newBirthday }

    fun register() {
        viewModelScope.launch {
            Log.d("DEBUG_DEBUG", "register")
            try {
                val response = RetrofitInstance.api.register(
                    email = email.value,
                    password = password.value,
                    name = parentName.value,
                    phone = phone.value,
                    childName = childName.value,
                    childBirthday = childBirthday.value
                )
                if (response.isSuccessful) {
                    Log.d("DEBUG_DEBUG", "register successful")
                    _isRegistered.value = true
                    _error.value = null
                } else {
                    _isRegistered.value = false
                    val error = response.errorBody()?.string()
                    Log.d("DEBUG_DEBUG", "register: $error")
                    _error.value = error ?: "Ошибка регистрации"
                }
            } catch (e: Exception) {
                _isRegistered.value = false
                Log.d("DEBUG_DEBUG", "register: ${e.message}")
                _error.value = e.message
            }
        }
    }
}