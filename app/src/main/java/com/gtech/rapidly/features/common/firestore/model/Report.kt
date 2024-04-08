package com.gtech.parcelpro.backend.data.model

import com.gtech.rapidly.features.common.firestore.model.Accounting

data class Report(
    val reportDate: String = "", // YYYY-MM-DD
    val report: Accounting = Accounting()
)