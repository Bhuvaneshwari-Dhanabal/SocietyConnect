package com.example.societyconnect.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminVisitorScreen(
    onBack: () -> Unit
) {

    val viewModel = remember {
        AdminVisitorViewModel()
    }

    val visitors by
    viewModel.visitors.collectAsState()

    val isLoading by
    viewModel.isLoading.collectAsState()

    val message by
    viewModel.message.collectAsState()


    LaunchedEffect(Unit) {

        viewModel.loadVisitors()
    }


    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Manage Visitors")
                },

                navigationIcon = {

                    OutlinedButton(
                        onClick = onBack
                    ) {
                        Text("Back")
                    }
                }
            )
        }

    ) { paddingValues ->

        if (isLoading && visitors.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),

                verticalArrangement =
                    Arrangement.Center
            ) {

                Text(
                    text = "Loading visitor requests..."
                )
            }

        } else {

            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),

                verticalArrangement =
                    Arrangement.spacedBy(12.dp)

            ) {

                item {

                    Text(
                        text = "Visitor Requests",
                        style =
                            MaterialTheme.typography
                                .headlineSmall
                    )

                    if (message != null) {

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text = message!!,
                            color =
                                MaterialTheme
                                    .colorScheme
                                    .primary
                        )
                    }
                }


                if (visitors.isEmpty()) {

                    item {

                        Text(
                            text = "No visitor requests found."
                        )
                    }

                } else {

                    items(visitors) { visitor ->

                        Card(
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Column(
                                modifier =
                                    Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text =
                                        visitor.visitorName,

                                    style =
                                        MaterialTheme
                                            .typography
                                            .titleLarge
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(6.dp)
                                )

                                Text(
                                    text =
                                        "Phone: ${visitor.phone}"
                                )

                                Text(
                                    text =
                                        "Date: ${visitor.visitDate}"
                                )

                                Text(
                                    text =
                                        "Time: ${visitor.visitTime}"
                                )

                                Text(
                                    text =
                                        "Purpose: ${visitor.purpose}"
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(6.dp)
                                )

                                Text(
                                    text =
                                        "Status: ${visitor.status}"
                                )


                                if (
                                    visitor.status ==
                                    "PENDING"
                                ) {

                                    Spacer(
                                        modifier =
                                            Modifier.height(12.dp)
                                    )

                                    Row(
                                        modifier =
                                            Modifier.fillMaxWidth(),

                                        horizontalArrangement =
                                            Arrangement.spacedBy(8.dp)
                                    ) {

                                        Button(

                                            onClick = {

                                                viewModel
                                                    .approveVisitor(
                                                        visitor.id
                                                    )
                                            },

                                            modifier =
                                                Modifier.weight(1f)
                                        ) {

                                            Text("Approve")
                                        }


                                        OutlinedButton(

                                            onClick = {

                                                viewModel
                                                    .rejectVisitor(
                                                        visitor.id
                                                    )
                                            },

                                            modifier =
                                                Modifier.weight(1f)
                                        ) {

                                            Text("Reject")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}