package com.example.societyconnect.data.repository

import com.example.societyconnect.data.model.Complaint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ComplaintRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()
) {

    fun submitComplaint(
        title: String,
        description: String,
        onResult: (Result<Unit>) -> Unit
    ) {

        val currentUser = auth.currentUser

        if (currentUser == null) {
            onResult(
                Result.failure(
                    Exception("User is not logged in")
                )
            )
            return
        }

        val complaint = Complaint(
            userId = currentUser.uid,
            title = title,
            description = description,
            status = "SUBMITTED",
            createdAt = System.currentTimeMillis()
        )

        firestore
            .collection("complaints")
            .add(complaint)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onResult(Result.success(Unit))
                } else {
                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception("Failed to submit complaint")
                        )
                    )
                }
            }
    }

    fun getMyComplaints(
        onResult: (Result<List<Complaint>>) -> Unit
    ) {

        val currentUser = auth.currentUser

        if (currentUser == null) {
            onResult(
                Result.failure(
                    Exception("User is not logged in")
                )
            )
            return
        }

        firestore
            .collection("complaints")
            .whereEqualTo("userId", currentUser.uid)
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
                                ?: Exception("Failed to load complaints")
                        )
                    )
                }
            }
    }
}