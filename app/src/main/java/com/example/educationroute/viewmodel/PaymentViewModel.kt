package com.example.educationroute.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.educationroute.network.RetrofitInstance
import kotlinx.coroutines.launch

class PaymentViewModel : ViewModel() {
    private val _paidLessons = mutableStateOf(0)
    val paidLessons: State<Int> = _paidLessons

    private val _selectedLessons = mutableStateOf(4)
    val selectedLessons: State<Int> = _selectedLessons

    private val _discounts = mutableStateOf(setOf<String>())
    val discounts: State<Set<String>> = _discounts

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    fun loadPaidLessons(clientId: Int) {
        viewModelScope.launch {
            try {
                val clients = RetrofitInstance.api.getClients()
                val client = clients.find { it.id == clientId }
                _paidLessons.value = client?.paidLessons ?: 0
            } catch (e: Exception) {
                _error.value = "Ошибка загрузки данных: ${e.message}"
            }
        }
    }

    fun setLessons(lessons: Int) {
        _selectedLessons.value = lessons
    }

    fun toggleDiscount(discount: String) {
        _discounts.value = if (discount in _discounts.value) {
            _discounts.value - discount
        } else {
            _discounts.value + discount
        }
    }

    fun pay(clientId: Int) {
        viewModelScope.launch {
            try {
                val newPaidLessons = _paidLessons.value + _selectedLessons.value
                val response = RetrofitInstance.api.updatePaidLessons(clientId, newPaidLessons)
                if (response.isSuccessful) {
                    _paidLessons.value = newPaidLessons
                } else {
                    _error.value = "Ошибка при оплате: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка при оплате: ${e.message}"
            }
        }
    }
}
