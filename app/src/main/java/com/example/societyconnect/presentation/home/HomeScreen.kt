package com.example.societyconnect.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class DashboardItem(
    val title: String,
    val description: String,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onFeatureClick: (String) -> Unit
) {

    val dashboardItems = listOf(
        DashboardItem(
            "Announcements",
            "View important society announcements",
            "announcements"
        ),
        DashboardItem(
            "Events",
            "View upcoming society events",
            "events"
        ),
        DashboardItem(
            "Complaints",
            "Submit and track complaints",
            "complaints"
        ),
        DashboardItem(
            "Payments",
            "View maintenance and payment details",
            "payments"
        ),
        DashboardItem(
            "Visitors",
            "Manage visitor information",
            "visitors"
        ),
        DashboardItem(
            "Profile",
            "View and manage your profile",
            "profile"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("SocietyConnect")
                }
            )
        }
    ) { paddingValues ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item(
                span = {
                    GridItemSpan(2)
                }
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Welcome to SocietyConnect!",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(20.dp)
                    )
                }
            }

            items(dashboardItems) { item ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onFeatureClick(item.route)
                        }
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            item(
                span = {
                    GridItemSpan(2)
                }
            ) {

                Button(
                    onClick = onLogout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Logout")
                }
            }
        }
    }
}