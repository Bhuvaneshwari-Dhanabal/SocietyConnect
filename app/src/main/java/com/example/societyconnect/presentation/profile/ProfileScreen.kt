package com.example.societyconnect.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "My Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        when (val state = uiState) {

            is ProfileUiState.Loading -> {

                CircularProgressIndicator()
            }

            is ProfileUiState.Success -> {

                val user = state.user

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = "Name",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Text(
                            text = user.name,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "Email",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "Phone",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Text(
                            text = user.phone,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "Flat Number",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Text(
                            text = user.flatNumber,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "Role",
                            style = MaterialTheme.typography.labelLarge
                        )

                        Text(
                            text = user.role,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            is ProfileUiState.Error -> {

                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error
                )

                Button(
                    onClick = {
                        viewModel.loadProfile()
                    }
                ) {
                    Text("Retry")
                }
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}