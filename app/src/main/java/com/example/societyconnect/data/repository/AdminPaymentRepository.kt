package com.example.societyconnect.data.repository

import com.example.societyconnect.data.model.Payment
import com.example.societyconnect.data.model.User
import com.google.firebase.firestore.FirebaseFirestore

class AdminPaymentRepository(
    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()
) {

    fun getResidents(
        onResult: (Result<List<User>>) -> Unit
    ) {

        firestore
            .collection("users")
            .whereEqualTo("role", "RESIDENT")
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val residents =
                        task.result?.documents?.map { document ->

                            User(
                                uid = document.id,
                                name = document.getString("name") ?: "",
                                email = document.getString("email") ?: "",
                                phone = document.getString("phone") ?: "",
                                flatNumber =
                                    document.getString("flatNumber") ?: "",
                                role = document.getString("role") ?: "RESIDENT"
                            )

                        } ?: emptyList()

                    onResult(Result.success(residents))

                } else {

                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception("Failed to load residents")
                        )
                    )
                }
            }
    }


    fun createPayment(
        userId: String,
        title: String,
        amount: Double,
        dueDate: String,
        onResult: (Result<Unit>) -> Unit
    ) {

        val payment = Payment(
            userId = userId,
            title = title,
            amount = amount,
            dueDate = dueDate,
            status = "PENDING",
            createdAt = System.currentTimeMillis()
        )

        firestore
            .collection("payments")
            .add(payment)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    onResult(Result.success(Unit))

                } else {

                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception("Failed to create payment")
                        )
                    )
                }
            }
    }


    fun getAllPayments(
        onResult: (Result<List<Payment>>) -> Unit
    ) {

        firestore
            .collection("payments")
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

                    onResult(Result.success(payments))

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


    fun updatePaymentStatus(
        paymentId: String,
        status: String,
        onResult: (Result<Unit>) -> Unit
    ) {

        firestore
            .collection("payments")
            .document(paymentId)
            .update("status", status)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    onResult(Result.success(Unit))

                } else {

                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception("Failed to update payment")
                        )
                    )
                }
            }
    }
}