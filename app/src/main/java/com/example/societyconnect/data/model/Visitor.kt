package com.example.societyconnect.data.model

data class Visitor(
    val id: String = "",
    val userId: String = "",
    val visitorName: String = "",
    val phone: String = "",
    val visitDate: String = "",
    val visitTime: String = "",
    val purpose: String = "",
    val status: String = "PENDING",
    val createdAt: Long = 0L
)