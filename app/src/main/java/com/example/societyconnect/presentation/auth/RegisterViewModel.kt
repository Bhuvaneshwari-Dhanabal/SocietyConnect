package com.example.societyconnect.presentation.auth

import androidx.lifecycle.ViewModel
import com.example.societyconnect.data.model.User
import com.example.societyconnect.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<AuthUiState>(AuthUiState.Idle)

    val uiState: StateFlow<AuthUiState> =
        _uiState.asStateFlow()

    fun register(
        name: String,
        email: String,
        phone: String,
        flatNumber: String,
        password: String,
        confirmPassword: String
    ) {

        if (name.isBlank()) {
            _uiState.value =
                AuthUiState.Error("Name is required")
            return
        }

        if (email.isBlank()) {
            _uiState.value =
                AuthUiState.Error("Email is required")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
        ) {
            _uiState.value =
                AuthUiState.Error(
                    "Please enter a valid email address"
                )
            return
        }

        if (phone.isBlank()) {
            _uiState.value =
                AuthUiState.Error("Phone number is required")
            return
        }

        if (flatNumber.isBlank()) {
            _uiState.value =
                AuthUiState.Error("Flat number is required")
            return
        }

        if (password.length < 6) {
            _uiState.value =
                AuthUiState.Error(
                    "Password must contain at least 6 characters"
                )
            return
        }

        if (password != confirmPassword) {
            _uiState.value =
                AuthUiState.Error(
                    "Passwords do not match"
                )
            return
        }

        _uiState.value =
            AuthUiState.Loading

        val user = User(
            name = name.trim(),
            email = email.trim(),
            phone = phone.trim(),
            flatNumber = flatNumber.trim(),
            role = "RESIDENT"
        )

        repository.register(
            user = user,
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

        val message = exception?.message.orEmpty()

        return when {

            message.contains(
                "email-already-in-use",
                ignoreCase = true
            ) ->
                "This email is already registered"

            message.contains(
                "invalid-email",
                ignoreCase = true
            ) ->
                "Please enter a valid email address"

            message.contains(
                "weak-password",
                ignoreCase = true
            ) ->
                "Password is too weak"

            message.contains(
                "network",
                ignoreCase = true
            ) ->
                "Network error. Check your internet connection"

            message.contains(
                "PERMISSION_DENIED",
                ignoreCase = true
            ) ->
                "Firestore permission denied"

            message.contains(
                "permission-denied",
                ignoreCase = true
            ) ->
                "Firestore permission denied"

            else ->
                "Registration failed: $message"
        }
    }
    }