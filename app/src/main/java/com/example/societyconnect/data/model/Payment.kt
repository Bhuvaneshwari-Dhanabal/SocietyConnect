package com.example.societyconnect.data.model

data class Payment(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val dueDate: String = "",
    val status: String = "PENDING",
    val createdAt: Long = 0L
)