package com.example.societyconnect.presentation.profile

import androidx.lifecycle.ViewModel
import com.example.societyconnect.data.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)

    val uiState: StateFlow<ProfileUiState> =
        _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {

        _uiState.value = ProfileUiState.Loading

        repository.getCurrentUserProfile { result ->

            _uiState.value =
                if (result.isSuccess) {

                    ProfileUiState.Success(
                        result.getOrThrow()
                    )

                } else {

                    ProfileUiState.Error(
                        result.exceptionOrNull()?.message
                            ?: "Failed to load profile"
                    )
                }
        }
    }
}