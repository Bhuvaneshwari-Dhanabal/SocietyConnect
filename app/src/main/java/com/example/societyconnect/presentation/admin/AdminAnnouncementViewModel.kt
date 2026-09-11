package com.example.societyconnect.presentation.admin

import androidx.lifecycle.ViewModel
import com.example.societyconnect.data.model.Announcement
import com.example.societyconnect.data.repository.AdminAnnouncementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed interface AdminAnnouncementUiState {

    data object Loading : AdminAnnouncementUiState

    data class Success(
        val announcements: List<Announcement>
    ) : AdminAnnouncementUiState

    data class Error(
        val message: String
    ) : AdminAnnouncementUiState
}

class AdminAnnouncementViewModel(
    private val repository: AdminAnnouncementRepository =
        AdminAnnouncementRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AdminAnnouncementUiState>(
            AdminAnnouncementUiState.Loading
        )

    val uiState: StateFlow<AdminAnnouncementUiState> =
        _uiState.asStateFlow()

    init {
        loadAnnouncements()
    }

    fun loadAnnouncements() {

        _uiState.value =
            AdminAnnouncementUiState.Loading

        repository.getAnnouncements { result ->

            _uiState.value =
                if (result.isSuccess) {

                    AdminAnnouncementUiState.Success(
                        result.getOrThrow()
                    )

                } else {

                    AdminAnnouncementUiState.Error(
                        result.exceptionOrNull()?.message
                            ?: "Failed to load announcements"
                    )
                }
        }
    }

    fun createAnnouncement(
        title: String,
        description: String,
        date: String,
        onCreated: () -> Unit
    ) {

        if (title.isBlank()) {
            _uiState.value =
                AdminAnnouncementUiState.Error(
                    "Title is required"
                )
            return
        }

        if (description.isBlank()) {
            _uiState.value =
                AdminAnnouncementUiState.Error(
                    "Description is required"
                )
            return
        }

        if (date.isBlank()) {
            _uiState.value =
                AdminAnnouncementUiState.Error(
                    "Date is required"
                )
            return
        }

        _uiState.value =
            AdminAnnouncementUiState.Loading

        repository.createAnnouncement(
            title = title.trim(),
            description = description.trim(),
            date = date.trim()
        ) { result ->

            if (result.isSuccess) {
                loadAnnouncements()
                onCreated()
            } else {
                _uiState.value =
                    AdminAnnouncementUiState.Error(
                        result.exceptionOrNull()?.message
                            ?: "Failed to create announcement"
                    )
            }
        }
    }
}