package com.example.societyconnect.data.repository

import com.example.societyconnect.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun login(
        email: String,
        password: String,
        onResult: (Result<FirebaseUser>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                val user = auth.currentUser

                if (user != null) {
                    onResult(Result.success(user))
                } else {
                    onResult(
                        Result.failure(
                            Exception("User not found")
                        )
                    )
                }

            } else {

                onResult(
                    Result.failure(
                        task.exception
                            ?: Exception("Login failed")
                    )
                )
            }
        }
    }

    fun register(
        user: User,
        password: String,
        onResult: (Result<FirebaseUser>) -> Unit
    ) {

        auth.createUserWithEmailAndPassword(
            user.email,
            password
        ).addOnCompleteListener { task ->

            if (!task.isSuccessful) {

                onResult(
                    Result.failure(
                        task.exception
                            ?: Exception("Registration failed")
                    )
                )

                return@addOnCompleteListener
            }

            val firebaseUser = auth.currentUser

            if (firebaseUser == null) {

                onResult(
                    Result.failure(
                        Exception("User creation failed")
                    )
                )

                return@addOnCompleteListener
            }

            val firestoreUser = user.copy(
                uid = firebaseUser.uid
            )

            firestore
                .collection("users")
                .document(firebaseUser.uid)
                .set(firestoreUser)
                .addOnCompleteListener { firestoreTask ->

                    if (firestoreTask.isSuccessful) {

                        onResult(
                            Result.success(firebaseUser)
                        )

                    } else {

                        onResult(
                            Result.failure(
                                firestoreTask.exception
                                    ?: Exception(
                                        "Failed to save user profile"
                                    )
                            )
                        )
                    }
                }
        }
    }

    fun logout() {
        auth.signOut()
    }
}