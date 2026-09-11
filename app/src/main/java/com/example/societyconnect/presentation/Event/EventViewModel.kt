package com.example.societyconnect.presentation.events

import androidx.lifecycle.ViewModel
import com.example.societyconnect.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EventViewModel(
    private val repository: EventRepository =
        EventRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<EventUiState>(
            EventUiState.Loading
        )

    val uiState: StateFlow<EventUiState> =
        _uiState.asStateFlow()

    init {
        loadEvents()
    }

    fun loadEvents() {

        _uiState.value = EventUiState.Loading

        repository.getEvents { result ->

            _uiState.value =
                if (result.isSuccess) {

                    EventUiState.Success(
                        result.getOrThrow()
                    )

                } else {

                    EventUiState.Error(
                        result.exceptionOrNull()?.message
                            ?: "Failed to load events"
                    )
                }
        }
    }
}