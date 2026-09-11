package com.example.societyconnect.presentation.complaints

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ComplaintScreen(
    onBack: () -> Unit,
    viewModel: ComplaintViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Complaints",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Submit a complaint and track its status.",
            modifier = Modifier.padding(top = 4.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
            },
            label = {
                Text("Complaint Title")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = {
                description = it
            },
            label = {
                Text("Description")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            minLines = 4
        )

        Button(
            onClick = {
                viewModel.submitComplaint(
                    title = title,
                    description = description,
                    onSubmitted = {
                        title = ""
                        description = ""
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Submit Complaint")
        }

        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = 16.dp
            )
        )

        Text(
            text = "My Complaints",
            style = MaterialTheme.typography.titleLarge
        )

        when (val state = uiState) {

            is ComplaintUiState.Loading -> {

                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            is ComplaintUiState.Success -> {

                if (state.complaints.isEmpty()) {

                    Text(
                        text = "You haven't submitted any complaints yet.",
                        modifier = Modifier.padding(top = 16.dp)
                    )

                } else {

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 12.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {

                        items(state.complaints) { complaint ->

                            ComplaintCard(
                                complaint = complaint
                            )
                        }
                    }
                }
            }

            is ComplaintUiState.Error -> {

                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Button(
                    onClick = {
                        viewModel.loadComplaints()
                    },
                    modifier = Modifier.padding(top = 8.dp)
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

@Composable
private fun ComplaintCard(
    complaint: com.example.societyconnect.data.model.Complaint
) {

    val formattedDate =
        if (complaint.createdAt > 0) {

            SimpleDateFormat(
                "dd MMM yyyy, hh:mm a",
                Locale.getDefault()
            ).format(
                Date(complaint.createdAt)
            )

        } else {
            "Unknown date"
        }

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
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = complaint.description,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Status: ${complaint.status}",
                style = MaterialTheme.typography.labelLarge
            )

            Text(
                text = "Submitted: $formattedDate",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}