package com.example.societyconnect.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
fun AdminComplaintScreen(
    onBack: () -> Unit,
    viewModel: AdminComplaintViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Manage Complaints",
            style = MaterialTheme.typography.headlineMedium
        )

        when (val state = uiState) {

            is AdminComplaintUiState.Loading -> {

                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 24.dp)
                )
            }

            is AdminComplaintUiState.Success -> {

                if (state.complaints.isEmpty()) {

                    Text(
                        text = "No complaints found.",
                        modifier = Modifier.padding(top = 24.dp)
                    )

                } else {

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 16.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        items(state.complaints) { complaint ->

                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {

                                    Text(
                                        text = complaint.title,
                                        style =
                                            MaterialTheme
                                                .typography
                                                .titleLarge
                                    )

                                    Text(
                                        text = complaint.description
                                    )

                                    Text(
                                        text = "Resident ID: ${complaint.userId}",
                                        style =
                                            MaterialTheme
                                                .typography
                                                .labelSmall
                                    )

                                    Text(
                                        text = "Status: ${complaint.status}",
                                        style =
                                            MaterialTheme
                                                .typography
                                                .titleMedium
                                    )

                                    if (complaint.status != "IN_PROGRESS") {

                                        Button(
                                            onClick = {
                                                viewModel.updateStatus(
                                                    complaint.id,
                                                    "IN_PROGRESS"
                                                )
                                            },
                                            modifier =
                                                Modifier.fillMaxWidth()
                                        ) {
                                            Text("Mark In Progress")
                                        }
                                    }

                                    if (complaint.status != "RESOLVED") {

                                        Button(
                                            onClick = {
                                                viewModel.updateStatus(
                                                    complaint.id,
                                                    "RESOLVED"
                                                )
                                            },
                                            modifier =
                                                Modifier.fillMaxWidth()
                                        ) {
                                            Text("Mark Resolved")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is AdminComplaintUiState.Error -> {

                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Button(
                    onClick = {
                        viewModel.loadComplaints()
                    },
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text("Retry")
                }
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Back")
        }
    }
}