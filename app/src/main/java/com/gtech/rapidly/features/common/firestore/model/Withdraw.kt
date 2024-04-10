package com.gtech.rapidly.features.common.firestore.model

import com.google.firebase.Timestamp
import java.util.UUID

data class Withdraw(
    val id: String = UUID.randomUUID().toString().uppercase(),
    val requestAmount: Double = 0.0,
    val approvedAmount: Double = 0.0,
    val requestNote: String = "",
    val approvedNote: String = "",
    val transactionId: String = "",
    val requestedBy: Long = 0,
    val approvedBy: Long = 0,
    val status: Status = Status.PENDING,
    var isSeen: Boolean = false,
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp? = null
) {
    enum class Status {
        PENDING,
        APPROVED,
        REJECTED
    }
}