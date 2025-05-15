package com.example.educationroute.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _currentClientId = MutableStateFlow<Int?>(null)
    val currentClientId: StateFlow<Int?> = _currentClientId.asStateFlow()

    init {
        Log.d("MainViewModel", "MainViewModel инициализирован")
        Log.d("MainViewModel", "Текущий clientId: ${_currentClientId.value}")
    }

    fun setClientId(id: Int) {
        Log.d("MainViewModel", "Установка clientId: $id")
        _currentClientId.value = id
        Log.d("MainViewModel", "clientId установлен: ${_currentClientId.value}")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MainViewModel", "MainViewModel очищен")
    }

    companion object {
        private var instance: MainViewModel? = null

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (instance == null) {
                    instance = MainViewModel()
                }
                return instance as T
            }
        }
    }
} 