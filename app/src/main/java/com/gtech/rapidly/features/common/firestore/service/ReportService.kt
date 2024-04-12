package com.gtech.rapidly.features.common.firestore.service

import com.gtech.rapidly.features.common.firestore.getAll
import com.gtech.rapidly.features.common.firestore.getByKey
import com.gtech.rapidly.features.common.firestore.model.Report
import com.gtech.rapidly.features.common.firestore.set
import com.gtech.rapidly.utils.error

object ReportService {

    private const val COLLECTION_NAME = "REPORT_HISTORY"

    suspend fun saveOrUpdate(report: Report): Boolean {
        return try {
            set(COLLECTION_NAME, report.reportDate, report)
            true
        } catch (e: Exception) {
            error(e)
            false
        }
    }

    suspend fun getReports(): List<Report> {
        return try {
            getAll(COLLECTION_NAME)
        } catch (e: Exception) {
            error(e)
            return emptyList()
        }
    }

    suspend fun getByDate(date: String): Report? {
        return try {
            getByKey(COLLECTION_NAME, date)
        } catch (e: Exception) {
            error(e)
            return null
        }
    }

}