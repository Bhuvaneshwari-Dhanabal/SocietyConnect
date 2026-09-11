package com.example.societyconnect.presentation.announcements

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
fun AnnouncementScreen(
    onBack: () -> Unit,
    viewModel: AnnouncementViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Announcements",
            style = MaterialTheme.typography.headlineMedium
        )

        when (val state = uiState) {

            is AnnouncementUiState.Loading -> {

                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 24.dp)
                )
            }

            is AnnouncementUiState.Success -> {

                if (state.announcements.isEmpty()) {

                    Text(
                        text = "No announcements available.",
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

                        items(state.announcements) { announcement ->

                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {

                                    Text(
                                        text = announcement.title,
                                        style =
                                            MaterialTheme.typography.titleLarge
                                    )

                                    Text(
                                        text = announcement.description,
                                        style =
                                            MaterialTheme.typography.bodyMedium
                                    )

                                    Text(
                                        text = "Date: ${announcement.date}",
                                        style =
                                            MaterialTheme.typography.labelMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is AnnouncementUiState.Error -> {

                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Button(
                    onClick = {
                        viewModel.loadAnnouncements()
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