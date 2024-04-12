package com.gtech.rapidly.features.common.firestore.model

import com.google.firebase.Timestamp
import java.util.UUID

data class Withdraw(
    val id: String = UUID.randomUUID().toString().uppercase(),
    val requestAmount: Double = 0.0,
    var approvedAmount: Double = 0.0,
    val requestNote: String = "",
    var attendeeNote: String = "",
    var transactionId: String = "",
    val requestedBy: Long = 0,
    var attendedBy: Long = 0,
    var status: Status = Status.PENDING,
    var isSeen: Boolean = false,
    val createdAt: Timestamp = Timestamp.now(),
    var updatedAt: Timestamp? = null
) {
    enum class Status {
        PENDING,
        APPROVED,
        REJECTED
    }
}