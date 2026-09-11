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

@Composable
fun AdminAnnouncementScreen(
    onBack: () -> Unit,
    viewModel: AdminAnnouncementViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var date by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Manage Announcements",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = {
                Text("Title")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = {
                Text("Description")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            minLines = 3
        )

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = {
                Text("Date (YYYY-MM-DD)")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )

        Button(
            onClick = {

                viewModel.createAnnouncement(
                    title = title,
                    description = description,
                    date = date,
                    onCreated = {
                        title = ""
                        description = ""
                        date = ""
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text("Create Announcement")
        }

        HorizontalDivider(
            modifier = Modifier.padding(
                vertical = 16.dp
            )
        )

        Text(
            text = "Existing Announcements",
            style = MaterialTheme.typography.titleLarge
        )

        when (val state = uiState) {

            is AdminAnnouncementUiState.Loading -> {

                CircularProgressIndicator(
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            is AdminAnnouncementUiState.Success -> {

                if (state.announcements.isEmpty()) {

                    Text(
                        text = "No announcements found.",
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

                        items(state.announcements) { announcement ->

                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {

                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {

                                    Text(
                                        text = announcement.title,
                                        style =
                                            MaterialTheme
                                                .typography
                                                .titleLarge
                                    )

                                    Text(
                                        text = announcement.description,
                                        modifier =
                                            Modifier.padding(top = 8.dp)
                                    )

                                    Text(
                                        text = "Date: ${announcement.date}",
                                        modifier =
                                            Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            is AdminAnnouncementUiState.Error -> {

                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
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
