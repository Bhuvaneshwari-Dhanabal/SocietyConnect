package com.example.societyconnect.data.repository

import com.example.societyconnect.data.model.Complaint
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AdminComplaintRepository(
    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()
) {

    fun getAllComplaints(
        onResult: (Result<List<Complaint>>) -> Unit
    ) {

        firestore
            .collection("complaints")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val complaints = task.result
                        ?.documents
                        ?.mapNotNull { document ->

                            document
                                .toObject(Complaint::class.java)
                                ?.copy(id = document.id)
                        }
                        ?: emptyList()

                    onResult(
                        Result.success(complaints)
                    )

                } else {

                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception(
                                    "Failed to load complaints"
                                )
                        )
                    )
                }
            }
    }

    fun updateComplaintStatus(
        complaintId: String,
        status: String,
        onResult: (Result<Unit>) -> Unit
    ) {

        firestore
            .collection("complaints")
            .document(complaintId)
            .update("status", status)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    onResult(Result.success(Unit))

                } else {

                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception(
                                    "Failed to update complaint"
                                )
                        )
                    )
                }
            }
    }
}