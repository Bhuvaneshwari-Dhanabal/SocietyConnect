package com.example.societyconnect.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminHomeScreen(
    onComplaintsClick: () -> Unit,
    onAnnouncementsClick: () -> Unit,
    onEventsClick: () -> Unit,
    onPaymentsClick: () -> Unit,
    onLogout: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Admin Dashboard",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Manage your society",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Complaints",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "View and manage resident complaints.",
                    modifier = Modifier.padding(top = 8.dp)
                )

                Button(
                    onClick = onComplaintsClick,
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text("Manage Complaints")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Announcements",
                    style = MaterialTheme.typography.titleLarge
                )

                Button(
                    onClick = onAnnouncementsClick,
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text("Manage Announcements")
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Events",
                    style = MaterialTheme.typography.titleLarge
                )

                Button(
                    onClick = onEventsClick,
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text("Manage Events")
                }
            }
        }

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }

        Button(
            onClick = onPaymentsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Manage Payments")
        }
    }
}