package com.gtech.rapidly.features.common.firestore.service

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.gtech.rapidly.features.common.firestore.model.Penalty
import com.gtech.rapidly.utils.error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object PenaltyService {

    private const val COLLECTION_NAME = "PENALTIES"

    suspend fun saveOrUpdate(
        penalty: Penalty
    ) = withContext(Dispatchers.IO) {
        try {
            FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(penalty.id)
                .set(penalty)
                .await()
            true
        } catch (e: Exception) {
            error(e)
            false
        }
    }

    fun getWithdrawals(
        phoneNumber: Long
    ) = try {
        FirebaseFirestore
            .getInstance()
            .collection(COLLECTION_NAME)
            .whereEqualTo("penalisedTo", phoneNumber)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(100)
    } catch (e: Exception) {
        error(e)
        null
    }

    fun getWithdrawals() = try {
        FirebaseFirestore
            .getInstance()
            .collection(COLLECTION_NAME)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(100)
    } catch (e: Exception) {
        error(e)
        null
    }

}