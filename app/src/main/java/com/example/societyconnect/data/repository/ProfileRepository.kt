package com.example.societyconnect.data.repository

import com.example.societyconnect.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    fun getCurrentUserProfile(
        onResult: (Result<User>) -> Unit
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
            .collection("users")
            .document(currentUser.uid)
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val user = task.result?.toObject(User::class.java)

                    if (user != null) {
                        onResult(Result.success(user))
                    } else {
                        onResult(
                            Result.failure(
                                Exception("User profile not found")
                            )
                        )
                    }

                } else {
                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception("Failed to load profile")
                        )
                    )
                }
            }
    }
}