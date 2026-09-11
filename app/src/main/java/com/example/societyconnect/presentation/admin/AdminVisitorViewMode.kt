package com.example.societyconnect.presentation.admin

import androidx.lifecycle.ViewModel
import com.example.societyconnect.data.model.Visitor
import com.example.societyconnect.data.repository.AdminVisitorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminVisitorViewModel(
    private val repository: AdminVisitorRepository =
        AdminVisitorRepository()
) : ViewModel() {

    private val _visitors =
        MutableStateFlow<List<Visitor>>(emptyList())

    val visitors: StateFlow<List<Visitor>> =
        _visitors


    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading


    private val _message =
        MutableStateFlow<String?>(null)

    val message: StateFlow<String?> =
        _message


    fun loadVisitors() {

        _isLoading.value = true

        repository.getAllVisitors { result ->

            _isLoading.value = false

            result
                .onSuccess {

                    _visitors.value = it
                }
                .onFailure {

                    _message.value =
                        it.message
                            ?: "Failed to load visitors"
                }
        }
    }


    fun approveVisitor(visitorId: String) {

        updateStatus(
            visitorId = visitorId,
            status = "APPROVED"
        )
    }


    fun rejectVisitor(visitorId: String) {

        updateStatus(
            visitorId = visitorId,
            status = "REJECTED"
        )
    }


    private fun updateStatus(
        visitorId: String,
        status: String
    ) {

        _isLoading.value = true

        repository.updateVisitorStatus(
            visitorId = visitorId,
            status = status
        ) { result ->

            _isLoading.value = false

            result
                .onSuccess {

                    _message.value =
                        "Visitor status updated"

                    loadVisitors()
                }
                .onFailure {

                    _message.value =
                        it.message
                            ?: "Failed to update visitor"
                }
        }
    }
}