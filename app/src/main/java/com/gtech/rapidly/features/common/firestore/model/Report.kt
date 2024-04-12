package com.gtech.rapidly.features.common.firestore.model

data class Report(
    val reportDate: String = "", // YYYY-MM-DD
    val report: Accounting = Accounting()
)