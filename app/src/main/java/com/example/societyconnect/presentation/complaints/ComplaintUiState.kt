package com.example.societyconnect.presentation.complaints

import com.example.societyconnect.data.model.Complaint

sealed interface ComplaintUiState {

    data object Loading : ComplaintUiState

    data class Success(
        val complaints: List<Complaint>
    ) : ComplaintUiState

    data class Error(
        val message: String
    ) : ComplaintUiState
}