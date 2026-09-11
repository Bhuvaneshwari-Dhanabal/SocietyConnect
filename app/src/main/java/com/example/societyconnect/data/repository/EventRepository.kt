package com.example.societyconnect.data.repository

import com.example.societyconnect.data.model.Event
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class EventRepository(
    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()
) {

    fun getEvents(
        onResult: (Result<List<Event>>) -> Unit
    ) {

        firestore
            .collection("events")
            .orderBy("date", Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val events = task.result
                        ?.documents
                        ?.mapNotNull { document ->

                            document.toObject(Event::class.java)
                                ?.copy(id = document.id)
                        }
                        ?: emptyList()

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
}