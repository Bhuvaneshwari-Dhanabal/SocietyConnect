package com.example.societyconnect.presentation.payments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    onBack: () -> Unit
) {

    val viewModel = remember {
        PaymentViewModel()
    }

    val uiState by
    viewModel.uiState.collectAsState()


    LaunchedEffect(Unit) {

        viewModel.loadPayments()
    }


    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("My Payments")
                },

                navigationIcon = {

                    androidx.compose.material3.TextButton(
                        onClick = onBack
                    ) {
                        Text("Back")
                    }
                }
            )
        }

    ) { paddingValues ->


        if (uiState.isLoading) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),

                verticalArrangement =
                    Arrangement.Center
            ) {

                Text(
                    text = "Loading payments..."
                )
            }

        } else if (uiState.errorMessage != null) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),

                verticalArrangement =
                    Arrangement.Center
            ) {

                Text(
                    text = uiState.errorMessage!!
                )
            }

        } else if (uiState.payments.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),

                verticalArrangement =
                    Arrangement.Center
            ) {

                Text(
                    text = "No payments found",
                    style =
                        MaterialTheme.typography.titleMedium
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

                items(uiState.payments) { payment ->

                    Card(
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = payment.title,
                                style =
                                    MaterialTheme.typography
                                        .titleLarge
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Text(
                                text =
                                    "Amount: ₹${payment.amount}"
                            )

                            Text(
                                text =
                                    "Due Date: ${payment.dueDate}"
                            )

                            Text(
                                text =
                                    "Status: ${payment.status}"
                            )
                        }
                    }
                }
            }
        }
    }
}