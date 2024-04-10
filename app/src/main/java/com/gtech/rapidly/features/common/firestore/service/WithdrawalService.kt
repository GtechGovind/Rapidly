package com.gtech.rapidly.features.common.firestore.service

import com.google.firebase.firestore.FirebaseFirestore
import com.gtech.rapidly.features.common.firestore.model.Withdraw
import com.gtech.rapidly.utils.error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object WithdrawalService {

    private const val COLLECTION_NAME = "WITHDRAWALS"

    suspend fun saveOrUpdate(
        withdrawal: Withdraw
    ) = withContext(Dispatchers.IO) {
        try {
            FirebaseFirestore
                .getInstance()
                .collection(COLLECTION_NAME)
                .document(withdrawal.id)
                .set(withdrawal)
                .await()
            true
        } catch (e: Exception) {
            error(e)
            false
        }
    }

    fun getWithdrawals(
        phoneNumber: Long
    ) = FirebaseFirestore
        .getInstance()
        .collection(COLLECTION_NAME)
        .whereEqualTo("requestedBy", phoneNumber)

}