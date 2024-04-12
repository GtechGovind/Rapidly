package com.gtech.rapidly.features.common.firestore.service

import com.gtech.rapidly.features.common.firestore.getAll
import com.gtech.rapidly.features.common.firestore.model.Legal
import com.gtech.rapidly.utils.error

object LegalService {

    private const val COLLECTION_NAME = "LEGAL"

    suspend fun getAll(): List<Legal> {
        return try {
            getAll(COLLECTION_NAME)
        } catch (e: Exception) {
            error(e)
            emptyList()
        }
    }

}