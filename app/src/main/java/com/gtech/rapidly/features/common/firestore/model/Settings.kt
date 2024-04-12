package com.gtech.rapidly.features.common.firestore.model

data class Settings(
    val deliveryBoyPercentage: Double = 0.0,
    val applicationVersion: String = "",
    val checkVersion: Boolean = false,
)
