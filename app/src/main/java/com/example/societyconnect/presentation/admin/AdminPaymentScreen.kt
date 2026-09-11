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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPaymentScreen(
    onBack: () -> Unit
) {

    val viewModel = remember {
        AdminPaymentViewModel()
    }

    val residents by
    viewModel.residents.collectAsState()

    val payments by
    viewModel.payments.collectAsState()

    val isLoading by
    viewModel.isLoading.collectAsState()

    val message by
    viewModel.message.collectAsState()


    var selectedResident by
    remember {
        mutableStateOf<com.example.societyconnect.data.model.User?>(
            null
        )
    }

    var residentMenuExpanded by
    remember {
        mutableStateOf(false)
    }

    var title by remember {
        mutableStateOf("")
    }

    var amount by remember {
        mutableStateOf("")
    }

    var dueDate by remember {
        mutableStateOf("")
    }


    LaunchedEffect(Unit) {
        viewModel.loadData()
    }


    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Manage Payments")
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

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)

        ) {


            // ==================================================
            // CREATE PAYMENT
            // ==================================================

            item {

                Text(
                    text = "Create Payment",
                    style =
                        MaterialTheme.typography.headlineSmall
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                // RESIDENT SELECTION

                OutlinedButton(

                    onClick = {
                        residentMenuExpanded = true
                    },

                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text =
                            selectedResident?.let {
                                "${it.name} - Flat ${it.flatNumber}"
                            }
                                ?: "Select Resident"
                    )
                }


                DropdownMenu(

                    expanded = residentMenuExpanded,

                    onDismissRequest = {
                        residentMenuExpanded = false
                    }

                ) {

                    residents.forEach { resident ->

                        androidx.compose.material3.DropdownMenuItem(

                            text = {

                                Text(
                                    "${resident.name} - Flat ${resident.flatNumber}"
                                )
                            },

                            onClick = {

                                selectedResident =
                                    resident

                                residentMenuExpanded =
                                    false
                            }
                        )
                    }
                }


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                OutlinedTextField(

                    value = title,

                    onValueChange = {
                        title = it
                    },

                    label = {
                        Text("Payment Title")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                OutlinedTextField(

                    value = amount,

                    onValueChange = {
                        amount = it
                    },

                    label = {
                        Text("Amount")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                OutlinedTextField(

                    value = dueDate,

                    onValueChange = {
                        dueDate = it
                    },

                    label = {
                        Text("Due Date (YYYY-MM-DD)")
                    },

                    modifier =
                        Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                Button(

                    onClick = {

                        viewModel.createPayment(

                            userId =
                                selectedResident?.uid
                                    ?: "",

                            title = title,

                            amountText = amount,

                            dueDate = dueDate
                        )
                    },

                    enabled = !isLoading,

                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text("Create Payment")
                }


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


                Spacer(
                    modifier =
                        Modifier.height(24.dp)
                )


                Text(
                    text = "All Payments",
                    style =
                        MaterialTheme.typography
                            .headlineSmall
                )
            }


            // ==================================================
            // PAYMENT LIST
            // ==================================================

            items(payments) { payment ->

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
                                Modifier.height(6.dp)
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
                                "Resident ID: ${payment.userId}"
                        )

                        Text(
                            text =
                                "Status: ${payment.status}"
                        )


                        if (payment.status == "PENDING") {

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            Row(
                                modifier =
                                    Modifier.fillMaxWidth(),
                                horizontalArrangement =
                                    Arrangement.End
                            ) {

                                Button(

                                    onClick = {

                                        viewModel.markAsPaid(
                                            payment.id
                                        )
                                    }

                                ) {

                                    Text("Mark Paid")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}