package com.example.educationroute.model

import kotlinx.serialization.Serializable

@Serializable
data class ClientDTO(
    val id: Int,
    val childName: String,
    val parentName: String,
    val age: Int,
    val phone: String,
    val paidLessons: Int?
) 