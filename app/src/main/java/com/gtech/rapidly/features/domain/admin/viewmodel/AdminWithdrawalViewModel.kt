package com.gtech.rapidly.features.domain.admin.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.model.Withdraw
import com.gtech.rapidly.features.common.firestore.service.AccountingService
import com.gtech.rapidly.features.common.firestore.service.UserService
import com.gtech.rapidly.features.common.firestore.service.WithdrawalService
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class AdminWithdrawalViewModel : ScreenModel() {

    private var processingItem: Pair<Withdraw, User> ?= null

    var showPendingRequests by mutableStateOf(false)
    var isProcessingDialogVisible by mutableStateOf(false)

    fun proceedToPay(
        approvedAmount: String,
        transactionId: String,
        isLoading: MutableState<Boolean>
    ) = screenModelScope.launch(Dispatchers.IO) {
        withLoading(isLoading) {

            if (processingItem == null) {
                showMessage("No item selected")
                return@withLoading
            }

            if (approvedAmount.isBlank()) {
                showMessage("Please enter the approved amount")
                return@withLoading
            }

            if (transactionId.isBlank()) {
                showMessage("Please enter the transaction ID")
                return@withLoading
            }

            val amount = approvedAmount.toDouble()
            if (amount <= 0) {
                showMessage("Please enter a valid amount")
                return@withLoading
            }

            val (withdraw, user) = processingItem!!

            // UPDATE WITHDRAW
            withdraw.approvedAmount = amount
            withdraw.transactionId = transactionId
            withdraw.isSeen = false
            withdraw.attendedBy = RuntimeCache.getCurrentUser().phoneNumber
            withdraw.status = Withdraw.Status.APPROVED
            withdraw.updatedAt = Timestamp.now()

            // UPDATE USER
            user.totalWithdrawal += amount

            // UPDATE ACCOUNT BALANCE
            val accounting = AccountingService.getReport()
            if (accounting == null) {
                showMessage("Failed to get accounting report")
                return@withLoading
            }

            // UPDATE ACCOUNTING
            accounting.totalDeliveryBoyWithdraw += amount

            // SAVE
            FirebaseFirestore
                .getInstance()
                .runTransaction {
                    runBlocking {
                        if(!UserService.saveOrUpdate(user)) throw Exception("Failed to update user")
                        if(!WithdrawalService.saveOrUpdate(withdraw)) throw Exception("Failed to update withdraw")
                        if(!AccountingService.saveOrUpdate(accounting)) throw Exception("Failed to update accounting")
                    }
                }

            showMessage("Withdrawal request approved")
            hideProcessWithdrawalDialog()

        }
    }

    fun rejectRequest(
        rejectedReason: String,
        isLoading: MutableState<Boolean>
    ) = screenModelScope.launch(Dispatchers.IO) {
        withLoading(isLoading) {

            if (processingItem == null) {
                showMessage("No item selected")
                return@withLoading
            }

            if (rejectedReason.isBlank()) {
                showMessage("Please enter the rejected reason")
                return@withLoading
            }

            val (withdraw, _) = processingItem!!

            // UPDATE WITHDRAW
            withdraw.attendeeNote = rejectedReason
            withdraw.isSeen = false
            withdraw.attendedBy = RuntimeCache.getCurrentUser().phoneNumber
            withdraw.status = Withdraw.Status.REJECTED
            withdraw.updatedAt = Timestamp.now()

            // SAVE
            if(!WithdrawalService.saveOrUpdate(withdraw)) {
                showMessage("Failed to reject withdrawal request")
            } else {
                showMessage("Withdrawal request rejected")
            }

            hideProcessWithdrawalDialog()

        }

    }

    fun showProcessWithdrawalDialog(
        item: Pair<Withdraw, User>
    ) {
        processingItem = item
        isProcessingDialogVisible = true
    }

    fun hideProcessWithdrawalDialog() {
        isProcessingDialogVisible = false
        processingItem = null
    }

}