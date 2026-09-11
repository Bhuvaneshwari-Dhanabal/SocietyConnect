package com.example.societyconnect.data.repository

import com.example.societyconnect.data.model.Payment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PaymentRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()
) {

    fun getMyPayments(
        onResult: (Result<List<Payment>>) -> Unit
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
            .collection("payments")
            .whereEqualTo("userId", currentUser.uid)
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val payments =
                        task.result?.documents?.map { document ->

                            Payment(
                                id = document.id,
                                userId =
                                    document.getString("userId") ?: "",
                                title =
                                    document.getString("title") ?: "",
                                amount =
                                    document.getDouble("amount") ?: 0.0,
                                dueDate =
                                    document.getString("dueDate") ?: "",
                                status =
                                    document.getString("status")
                                        ?: "PENDING",
                                createdAt =
                                    document.getLong("createdAt") ?: 0L
                            )

                        } ?: emptyList()

                    onResult(
                        Result.success(payments)
                    )

                } else {

                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception("Failed to load payments")
                        )
                    )
                }
            }
    }
}