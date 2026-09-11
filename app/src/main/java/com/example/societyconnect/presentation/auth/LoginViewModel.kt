package com.example.societyconnect.presentation.auth

import androidx.lifecycle.ViewModel
import com.example.societyconnect.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AuthUiState>(AuthUiState.Idle)

    val uiState: StateFlow<AuthUiState> =
        _uiState.asStateFlow()

    fun login(
        email: String,
        password: String
    ) {

        // Validate email
        if (email.isBlank()) {
            _uiState.value =
                AuthUiState.Error("Email is required")
            return
        }

        // Validate password
        if (password.isBlank()) {
            _uiState.value =
                AuthUiState.Error("Password is required")
            return
        }

        // Show loading state
        _uiState.value =
            AuthUiState.Loading

        // Call Firebase through Repository
        repository.login(
            email = email,
            password = password
        ) { result ->

            _uiState.value =
                if (result.isSuccess) {

                    AuthUiState.Success

                } else {

                    AuthUiState.Error(
                        getErrorMessage(
                            result.exceptionOrNull()
                        )
                    )
                }
        }
    }

    private fun getErrorMessage(
        exception: Throwable?
    ): String {

        val message =
            exception?.message.orEmpty()

        return when {

            message.contains(
                "invalid-credential",
                ignoreCase = true
            ) ->
                "Invalid email or password"

            message.contains(
                "invalid-email",
                ignoreCase = true
            ) ->
                "Please enter a valid email address"

            message.contains(
                "user-not-found",
                ignoreCase = true
            ) ->
                "No account found with this email"

            message.contains(
                "network",
                ignoreCase = true
            ) ->
                "Network error. Please check your connection."

            else ->
                "Login failed. Please try again."
        }
    }
}