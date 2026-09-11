package com.example.societyconnect.presentation.events

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
fun EventScreen(
    onBack: () -> Unit,
    viewModel: EventViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Events",
            style = MaterialTheme.typography.headlineMedium
        )

        when (val state = uiState) {

            is EventUiState.Loading -> {

                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 24.dp)
                )
            }

            is EventUiState.Success -> {

                if (state.events.isEmpty()) {

                    Text(
                        text = "No upcoming events.",
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

                        items(state.events) { event ->

                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {

                                    Text(
                                        text = event.title,
                                        style =
                                            MaterialTheme.typography.titleLarge
                                    )

                                    Text(
                                        text = event.description,
                                        style =
                                            MaterialTheme.typography.bodyMedium
                                    )

                                    Text(
                                        text = "Date: ${event.date}",
                                        style =
                                            MaterialTheme.typography.labelMedium
                                    )

                                    Text(
                                        text = "Time: ${event.time}",
                                        style =
                                            MaterialTheme.typography.labelMedium
                                    )

                                    Text(
                                        text = "Location: ${event.location}",
                                        style =
                                            MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is EventUiState.Error -> {

                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Button(
                    onClick = {
                        viewModel.loadEvents()
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