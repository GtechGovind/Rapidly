package com.gtech.rapidly.features.common.firestore.model

import com.google.firebase.Timestamp
import java.io.Serializable
import java.util.UUID

data class Penalty(
    val id: String = UUID.randomUUID().toString(),
    val amount: Double = 0.0,
    val reason: String = "",
    val penalisedTo: Long = 0,
    val penalisedBy: Long = 0,
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp? = null
): Serializable