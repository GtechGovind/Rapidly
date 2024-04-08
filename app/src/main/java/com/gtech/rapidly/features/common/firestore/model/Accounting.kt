package com.gtech.rapidly.features.common.firestore.model

data class Accounting(
    val name: String = "",
    var totalOrderAmount: Double = 0.0,
    var totalCompanyEarning: Double = 0.0,
    var totalCompanyCommission: Double = 0.0,
    var totalDeliveryBoyCommission: Double = 0.0,
)