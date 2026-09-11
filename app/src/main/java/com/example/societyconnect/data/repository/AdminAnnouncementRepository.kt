package com.example.societyconnect.data.repository

import com.example.societyconnect.data.model.Announcement
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AdminAnnouncementRepository(
    private val firestore: FirebaseFirestore =
        FirebaseFirestore.getInstance()
) {

    fun getAnnouncements(
        onResult: (Result<List<Announcement>>) -> Unit
    ) {
        firestore
            .collection("announcements")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val announcements = task.result
                        ?.documents
                        ?.mapNotNull { document ->
                            document
                                .toObject(Announcement::class.java)
                                ?.copy(id = document.id)
                        }
                        ?: emptyList()

                    onResult(Result.success(announcements))

                } else {

                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception(
                                    "Failed to load announcements"
                                )
                        )
                    )
                }
            }
    }

    fun createAnnouncement(
        title: String,
        description: String,
        date: String,
        onResult: (Result<Unit>) -> Unit
    ) {

        val announcement = Announcement(
            title = title,
            description = description,
            date = date,
            createdBy = "ADMIN"
        )

        firestore
            .collection("announcements")
            .add(announcement)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    onResult(Result.success(Unit))
                } else {
                    onResult(
                        Result.failure(
                            task.exception
                                ?: Exception(
                                    "Failed to create announcement"
                                )
                        )
                    )
                }
            }
    }
}