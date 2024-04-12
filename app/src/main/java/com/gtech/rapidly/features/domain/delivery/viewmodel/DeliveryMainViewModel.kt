package com.gtech.rapidly.features.domain.delivery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.firestore.ListenerRegistration
import com.gtech.rapidly.features.common.firestore.model.Withdraw
import com.gtech.rapidly.features.common.firestore.service.WithdrawalService
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import com.gtech.rapidly.features.common.notification.SystemNotificationService
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.runBlocking

class DeliveryMainViewModel : ScreenModel() {

    var title by mutableStateOf("Tej Delivery")
    var withdrawals = mutableStateListOf<Withdraw>()
    var withdrawalsNotification by mutableIntStateOf(0)

    private var listener: ListenerRegistration? = null

    companion object {
        var instance: DeliveryMainViewModel? = null
            private set
    }

    override suspend fun onCreated() {
        super.onCreated()
        instance = this
        startListeningToWithdrawals()
    }

    private fun startListeningToWithdrawals() {
        listener = WithdrawalService.getWithdrawals(
            RuntimeCache.getCurrentUser().phoneNumber
        )?.addSnapshotListener { value, error ->
            error?.let {
                runBlocking { handleError(it) }
                return@addSnapshotListener
            }
            value?.let {
                withdrawals.clear()
                withdrawalsNotification = 0
                value.documents.forEach { doc ->
                    withdrawals.add(doc.toObject(Withdraw::class.java)!!)
                }
                withdrawals.filter { it.isSeen.not() }.forEach { _ ->
                    withdrawalsNotification++
                    SystemNotificationService.notify(
                        title = "Withdrawal Request",
                        message = "You have a new withdrawal request",
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        listener?.remove()
        super.onDestroy()
    }

}
