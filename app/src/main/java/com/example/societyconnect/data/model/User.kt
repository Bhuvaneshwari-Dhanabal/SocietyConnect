package com.example.societyconnect.data.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val flatNumber: String = "",
    val role: String = "RESIDENT"
)