package com.example.societyconnect.presentation.announcements

import com.example.societyconnect.data.model.Announcement

sealed interface AnnouncementUiState {

    data object Loading : AnnouncementUiState

    data class Success(
        val announcements: List<Announcement>
    ) : AnnouncementUiState

    data class Error(
        val message: String
    ) : AnnouncementUiState
}