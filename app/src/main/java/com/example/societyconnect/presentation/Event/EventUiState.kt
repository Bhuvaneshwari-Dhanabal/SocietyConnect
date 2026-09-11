package com.example.societyconnect.presentation.events

import com.example.societyconnect.data.model.Event

sealed interface EventUiState {

    data object Loading : EventUiState

    data class Success(
        val events: List<Event>
    ) : EventUiState

    data class Error(
        val message: String
    ) : EventUiState
}