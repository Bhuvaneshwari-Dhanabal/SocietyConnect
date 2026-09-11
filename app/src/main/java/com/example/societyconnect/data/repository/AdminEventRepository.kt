package com.example.societyconnect.data.repository

import com.example.societyconnect.data.model.Event
import com.google.firebase.firestore.FirebaseFirestore

class AdminEventRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun getAllEvents(
        onResult: (Result<List<Event>>) -> Unit
    ) {

        firestore
            .collection("events")
            .orderBy("date")
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val events = task.result?.documents?.map { document ->

                        Event(
                            id = document.id,
                            title = document.getString("title") ?: "",
                            description = document.getString("description") ?: "",
                            date = document.getString("date") ?: "",
                            time = document.getString("time") ?: "",
                            location = document.getString("location") ?: "",
                            createdBy = document.getString("createdBy") ?: ""
                        )

                    } ?: emptyList()

                    onResult(Result.success(events))

                } else {

                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception("Failed to load events")
                        )
                    )
                }
            }
    }


    fun createEvent(
        title: String,
        description: String,
        date: String,
        time: String,
        location: String,
        onResult: (Result<Unit>) -> Unit
    ) {

        val event = Event(
            title = title,
            description = description,
            date = date,
            time = time,
            location = location,
            createdBy = "ADMIN"
        )

        firestore
            .collection("events")
            .add(event)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    onResult(Result.success(Unit))

                } else {

                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception("Failed to create event")
                        )
                    )
                }
            }
    }


    fun deleteEvent(
        eventId: String,
        onResult: (Result<Unit>) -> Unit
    ) {

        firestore
            .collection("events")
            .document(eventId)
            .delete()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    onResult(Result.success(Unit))

                } else {

                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception("Failed to delete event")
                        )
                    )
                }
            }
    }
}