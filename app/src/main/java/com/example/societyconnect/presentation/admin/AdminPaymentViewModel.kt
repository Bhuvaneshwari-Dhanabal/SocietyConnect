package com.example.societyconnect.presentation.admin

import androidx.lifecycle.ViewModel
import com.example.societyconnect.data.model.Payment
import com.example.societyconnect.data.model.User
import com.example.societyconnect.data.repository.AdminPaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminPaymentViewModel(
    private val repository: AdminPaymentRepository =
        AdminPaymentRepository()
) : ViewModel() {

    private val _residents =
        MutableStateFlow<List<User>>(emptyList())

    val residents: StateFlow<List<User>> = _residents


    private val _payments =
        MutableStateFlow<List<Payment>>(emptyList())

    val payments: StateFlow<List<Payment>> = _payments


    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> = _isLoading


    private val _message =
        MutableStateFlow<String?>(null)

    val message: StateFlow<String?> = _message


    fun loadData() {

        _isLoading.value = true

        repository.getResidents { result ->

            result
                .onSuccess {
                    _residents.value = it
                }
                .onFailure {
                    _message.value =
                        it.message ?: "Failed to load residents"
                }

            repository.getAllPayments { paymentResult ->

                _isLoading.value = false

                paymentResult
                    .onSuccess {
                        _payments.value = it
                    }
                    .onFailure {
                        _message.value =
                            it.message
                                ?: "Failed to load payments"
                    }
            }
        }
    }


    fun createPayment(
        userId: String,
        title: String,
        amountText: String,
        dueDate: String
    ) {

        if (userId.isBlank()) {
            _message.value =
                "Please select a resident"
            return
        }

        if (title.isBlank()) {
            _message.value =
                "Please enter payment title"
            return
        }

        val amount = amountText.toDoubleOrNull()

        if (amount == null || amount <= 0) {
            _message.value =
                "Please enter a valid amount"
            return
        }

        if (dueDate.isBlank()) {
            _message.value =
                "Please enter due date"
            return
        }

        _isLoading.value = true

        repository.createPayment(
            userId = userId,
            title = title,
            amount = amount,
            dueDate = dueDate
        ) { result ->

            _isLoading.value = false

            result
                .onSuccess {

                    _message.value =
                        "Payment created successfully"

                    loadData()
                }
                .onFailure {

                    _message.value =
                        it.message
                            ?: "Failed to create payment"
                }
        }
    }


    fun markAsPaid(paymentId: String) {

        _isLoading.value = true

        repository.updatePaymentStatus(
            paymentId = paymentId,
            status = "PAID"
        ) { result ->

            _isLoading.value = false

            result
                .onSuccess {

                    _message.value =
                        "Payment marked as paid"

                    loadData()
                }
                .onFailure {

                    _message.value =
                        it.message
                            ?: "Failed to update payment"
                }
        }
    }


    fun clearMessage() {
        _message.value = null
    }
}