package com.example.societyconnect.presentation.payments

import androidx.lifecycle.ViewModel
import com.example.societyconnect.data.repository.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PaymentViewModel(
    private val repository: PaymentRepository =
        PaymentRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(PaymentUiState())

    val uiState: StateFlow<PaymentUiState> =
        _uiState


    fun loadPayments() {

        _uiState.value =
            _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

        repository.getMyPayments { result ->

            result
                .onSuccess { payments ->

                    _uiState.value =
                        PaymentUiState(
                            isLoading = false,
                            payments = payments
                        )
                }

                .onFailure { exception ->

                    _uiState.value =
                        PaymentUiState(
                            isLoading = false,
                            errorMessage =
                                exception.message
                                    ?: "Failed to load payments"
                        )
                }
        }
    }
}