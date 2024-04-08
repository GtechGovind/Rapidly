package com.gtech.rapidly.features.common.firestore.service

import com.gtech.rapidly.features.common.firestore.getByKey
import com.gtech.rapidly.features.common.firestore.model.Accounting
import com.gtech.rapidly.features.common.firestore.set
import com.gtech.rapidly.utils.error

object AccountingService {

    private const val COLLECTION_NAME = "ACCOUNT"
    private const val COLLECTION_VERSION = "V2"

    suspend fun saveOrUpdate(accounting: Accounting): Boolean {
        return try {
            set(COLLECTION_NAME, COLLECTION_VERSION, accounting)
            true
        } catch (e: Exception) {
            error(e)
            false
        }
    }

    suspend fun getReport(id: String = COLLECTION_VERSION): Accounting? {
        return try {
            getByKey(COLLECTION_NAME, id)
        } catch (e: Exception) {
            error(e)
            return null
        }
    }

}