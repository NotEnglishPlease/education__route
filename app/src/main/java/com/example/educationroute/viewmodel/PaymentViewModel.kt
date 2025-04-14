package com.example.educationroute.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class PaymentViewModel : ViewModel() {
    private val _paidLessons = mutableStateOf(0)
    val paidLessons: State<Int> = _paidLessons

    private val _selectedLessons = mutableStateOf(4)
    val selectedLessons: State<Int> = _selectedLessons

    private val _discounts = mutableStateOf(setOf<String>())
    val discounts: State<Set<String>> = _discounts

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

    fun pay() {
        _paidLessons.value += _selectedLessons.value
    }
}
