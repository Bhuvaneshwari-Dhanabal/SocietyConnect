package com.example.societyconnect.presentation.complaints

import androidx.lifecycle.ViewModel
import com.example.societyconnect.data.repository.ComplaintRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ComplaintViewModel(
    private val repository: ComplaintRepository =
        ComplaintRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ComplaintUiState>(
            ComplaintUiState.Loading
        )

    val uiState: StateFlow<ComplaintUiState> =
        _uiState.asStateFlow()

    init {
        loadComplaints()
    }

    fun submitComplaint(
        title: String,
        description: String,
        onSubmitted: () -> Unit
    ) {

        if (title.isBlank()) {
            _uiState.value =
                ComplaintUiState.Error(
                    "Complaint title is required"
                )
            return
        }

        if (description.isBlank()) {
            _uiState.value =
                ComplaintUiState.Error(
                    "Complaint description is required"
                )
            return
        }

        _uiState.value =
            ComplaintUiState.Loading

        repository.submitComplaint(
            title = title.trim(),
            description = description.trim()
        ) { result ->

            if (result.isSuccess) {
                loadComplaints()
                onSubmitted()
            } else {
                _uiState.value =
                    ComplaintUiState.Error(
                        result.exceptionOrNull()?.message
                            ?: "Failed to submit complaint"
                    )
            }
        }
    }

    fun loadComplaints() {

        _uiState.value =
            ComplaintUiState.Loading

        repository.getMyComplaints { result ->

            _uiState.value =
                if (result.isSuccess) {

                    ComplaintUiState.Success(
                        result.getOrThrow()
                    )

                } else {

                    ComplaintUiState.Error(
                        result.exceptionOrNull()?.message
                            ?: "Failed to load complaints"
                    )
                }
        }
    }
}