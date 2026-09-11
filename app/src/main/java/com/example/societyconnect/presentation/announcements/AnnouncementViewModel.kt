package com.example.societyconnect.presentation.announcements

import androidx.lifecycle.ViewModel
import com.example.societyconnect.data.repository.AnnouncementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AnnouncementViewModel(
    private val repository: AnnouncementRepository =
        AnnouncementRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AnnouncementUiState>(
            AnnouncementUiState.Loading
        )

    val uiState: StateFlow<AnnouncementUiState> =
        _uiState.asStateFlow()

    init {
        loadAnnouncements()
    }

    fun loadAnnouncements() {

        _uiState.value =
            AnnouncementUiState.Loading

        repository.getAnnouncements { result ->

            _uiState.value =
                if (result.isSuccess) {

                    AnnouncementUiState.Success(
                        result.getOrThrow()
                    )

                } else {

                    AnnouncementUiState.Error(
                        result.exceptionOrNull()?.message
                            ?: "Failed to load announcements"
                    )
                }
        }
    }
}