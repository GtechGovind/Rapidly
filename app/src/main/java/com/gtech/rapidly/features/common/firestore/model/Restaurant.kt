package com.gtech.rapidly.features.common.firestore.model

import com.google.firebase.Timestamp

data class Restaurant(
    val id: Long = 0,
    val name: String = "",
    val logo: String = "",
    val location: String = "",
    val since: String = "",
    val uniqueIdentifier: String = "",
    val contactNumber: String = "",
    val income: Double = 0.0,
    val percentage: Double = 0.0,
    val timestamp: Timestamp = Timestamp.now(),
)
