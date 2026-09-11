package com.example.societyconnect.presentation.payments

import com.example.societyconnect.data.model.Payment

data class PaymentUiState(
    val isLoading: Boolean = false,
    val payments: List<Payment> = emptyList(),
    val errorMessage: String? = null
)