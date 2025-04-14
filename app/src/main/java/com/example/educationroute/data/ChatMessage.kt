package com.example.educationroute.data

import java.text.SimpleDateFormat
import java.util.*

data class ChatMessage(
    val id: Int,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Date
) {
    fun formattedTime(): String {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(timestamp)
    }
}