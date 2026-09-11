package com.example.societyconnect.presentation.admin
import androidx.compose.material3.ExperimentalMaterial3Api
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
fun AdminEventScreen(
    onBack: () -> Unit
) {

    val viewModel = remember {
        AdminEventViewModel()
    }

    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()


    var title by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var date by remember {
        mutableStateOf("")
    }

    var time by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf("")
    }


    LaunchedEffect(Unit) {

        viewModel.loadEvents()
    }


    Scaffold(

        topBar = {

            TopAppBar(

                title = {
                    Text("Manage Events")
                },

                navigationIcon = {

                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.padding(start = 8.dp)
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

            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {


            // ==================================================
            // CREATE EVENT
            // ==================================================

            item {

                Text(
                    text = "Create New Event",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = {
                        Text("Event Title")
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = {
                        Text("Description")
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                OutlinedTextField(
                    value = date,
                    onValueChange = {
                        date = it
                    },
                    label = {
                        Text("Date (YYYY-MM-DD)")
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                OutlinedTextField(
                    value = time,
                    onValueChange = {
                        time = it
                    },
                    label = {
                        Text("Time")
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                OutlinedTextField(
                    value = location,
                    onValueChange = {
                        location = it
                    },
                    label = {
                        Text("Location")
                    },
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                Button(

                    onClick = {

                        viewModel.createEvent(
                            title = title,
                            description = description,
                            date = date,
                            time = time,
                            location = location
                        )

                    },

                    enabled = !isLoading,

                    modifier = Modifier.fillMaxWidth()

                ) {

                    Text("Create Event")
                }


                if (message != null) {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = message!!,
                        color = MaterialTheme.colorScheme.primary
                    )
                }


                Spacer(
                    modifier = Modifier.height(24.dp)
                )


                Text(
                    text = "Existing Events",
                    style = MaterialTheme.typography.headlineSmall
                )
            }


            // ==================================================
            // EXISTING EVENTS
            // ==================================================

            items(events) { event ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = event.title,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = event.description
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = "Date: ${event.date}"
                        )

                        Text(
                            text = "Time: ${event.time}"
                        )

                        Text(
                            text = "Location: ${event.location}"
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            OutlinedButton(

                                onClick = {

                                    viewModel.deleteEvent(
                                        event.id
                                    )
                                }

                            ) {

                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}