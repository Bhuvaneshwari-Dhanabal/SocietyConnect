package com.example.societyconnect.presentation.admin

import androidx.lifecycle.ViewModel
import com.example.societyconnect.data.model.Event
import com.example.societyconnect.data.repository.AdminEventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AdminEventViewModel(
    private val repository: AdminEventRepository = AdminEventRepository()
) : ViewModel() {

    private val _events =
        MutableStateFlow<List<Event>>(emptyList())

    val events: StateFlow<List<Event>> = _events


    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> = _isLoading


    private val _message =
        MutableStateFlow<String?>(null)

    val message: StateFlow<String?> = _message


    fun loadEvents() {

        _isLoading.value = true

        repository.getAllEvents { result ->

            _isLoading.value = false

            result
                .onSuccess { eventList ->

                    _events.value = eventList
                }
                .onFailure { exception ->

                    _message.value =
                        exception.message
                            ?: "Failed to load events"
                }
        }
    }


    fun createEvent(
        title: String,
        description: String,
        date: String,
        time: String,
        location: String
    ) {

        if (title.isBlank()) {

            _message.value = "Please enter event title"
            return
        }

        if (description.isBlank()) {

            _message.value = "Please enter event description"
            return
        }

        if (date.isBlank()) {

            _message.value = "Please enter event date"
            return
        }

        if (time.isBlank()) {

            _message.value = "Please enter event time"
            return
        }

        if (location.isBlank()) {

            _message.value = "Please enter event location"
            return
        }

        _isLoading.value = true

        repository.createEvent(
            title = title,
            description = description,
            date = date,
            time = time,
            location = location
        ) { result ->

            _isLoading.value = false

            result
                .onSuccess {

                    _message.value =
                        "Event created successfully"

                    loadEvents()
                }
                .onFailure { exception ->

                    _message.value =
                        exception.message
                            ?: "Failed to create event"
                }
        }
    }


    fun deleteEvent(eventId: String) {

        _isLoading.value = true

        repository.deleteEvent(eventId) { result ->

            _isLoading.value = false

            result
                .onSuccess {

                    _message.value =
                        "Event deleted successfully"

                    loadEvents()
                }
                .onFailure { exception ->

                    _message.value =
                        exception.message
                            ?: "Failed to delete event"
                }
        }
    }


    fun clearMessage() {

        _message.value = null
    }
}