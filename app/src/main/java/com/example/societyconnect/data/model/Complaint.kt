package com.example.societyconnect.data.model

data class Complaint(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val status: String = "SUBMITTED",
    val createdAt: Long = 0L
)