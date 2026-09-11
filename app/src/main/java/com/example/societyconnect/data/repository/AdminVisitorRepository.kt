package com.example.societyconnect.data.repository

import com.example.societyconnect.data.model.Visitor
import com.google.firebase.firestore.FirebaseFirestore

class AdminVisitorRepository(
    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()
) {

    fun getAllVisitors(
        onResult: (Result<List<Visitor>>) -> Unit
    ) {

        firestore
            .collection("visitors")
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val visitors =
                        task.result?.documents?.map { document ->

                            Visitor(
                                id = document.id,
                                userId =
                                    document.getString("userId")
                                        ?: "",
                                visitorName =
                                    document.getString("visitorName")
                                        ?: "",
                                phone =
                                    document.getString("phone")
                                        ?: "",
                                visitDate =
                                    document.getString("visitDate")
                                        ?: "",
                                visitTime =
                                    document.getString("visitTime")
                                        ?: "",
                                purpose =
                                    document.getString("purpose")
                                        ?: "",
                                status =
                                    document.getString("status")
                                        ?: "PENDING",
                                createdAt =
                                    document.getLong("createdAt")
                                        ?: 0L
                            )

                        } ?: emptyList()

                    onResult(Result.success(visitors))

                } else {

                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception(
                                    "Failed to load visitor requests"
                                )
                        )
                    )
                }
            }
    }


    fun updateVisitorStatus(
        visitorId: String,
        status: String,
        onResult: (Result<Unit>) -> Unit
    ) {

        firestore
            .collection("visitors")
            .document(visitorId)
            .update("status", status)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    onResult(Result.success(Unit))

                } else {

                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception(
                                    "Failed to update visitor status"
                                )
                        )
                    )
                }
            }
    }
}