package com.example.societyconnect.presentation.admin

import androidx.lifecycle.ViewModel
import com.example.societyconnect.data.repository.AdminComplaintRepository
import com.example.societyconnect.data.model.Complaint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface AdminComplaintUiState {

    data object Loading : AdminComplaintUiState

    data class Success(
        val complaints: List<Complaint>
    ) : AdminComplaintUiState

    data class Error(
        val message: String
    ) : AdminComplaintUiState
}

class AdminComplaintViewModel(
    private val repository: AdminComplaintRepository =
        AdminComplaintRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AdminComplaintUiState>(
            AdminComplaintUiState.Loading
        )

    val uiState: StateFlow<AdminComplaintUiState> =
        _uiState.asStateFlow()

    init {
        loadComplaints()
    }

    fun loadComplaints() {

        _uiState.value =
            AdminComplaintUiState.Loading

        repository.getAllComplaints { result ->

            _uiState.value =
                if (result.isSuccess) {

                    AdminComplaintUiState.Success(
                        result.getOrThrow()
                    )

                } else {

                    AdminComplaintUiState.Error(
                        result.exceptionOrNull()?.message
                            ?: "Failed to load complaints"
                    )
                }
        }
    }

    fun updateStatus(
        complaintId: String,
        status: String
    ) {

        repository.updateComplaintStatus(
            complaintId = complaintId,
            status = status
        ) { result ->

            if (result.isSuccess) {
                loadComplaints()
            } else {
                _uiState.value =
                    AdminComplaintUiState.Error(
                        result.exceptionOrNull()?.message
                            ?: "Failed to update status"
                    )
            }
        }
    }
}