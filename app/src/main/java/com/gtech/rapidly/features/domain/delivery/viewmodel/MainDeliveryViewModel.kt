package com.gtech.rapidly.features.domain.delivery.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.gtech.rapidly.features.common.firestore.model.Withdraw
import com.gtech.rapidly.features.common.firestore.service.WithdrawalService
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import com.gtech.rapidly.features.common.notification.SystemNotificationService
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.launch

class MainDeliveryViewModel : ScreenModel() {

    var title by mutableStateOf("Tej Delivery")
    var withdrawals = mutableStateListOf<Withdraw>()
    var withdrawalsNotification by mutableIntStateOf(0)

    private lateinit var listener: ListenerRegistration

    companion object {
        var instance: MainDeliveryViewModel? = null
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
        )
            .addSnapshotListener { value, error ->
                error?.let {
                    screenModelScope.launch {
                        showMessage(it.message ?: "Failed to get withdrawals")
                    }
                    return@addSnapshotListener
                }

                value?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(Withdraw::class.java)
                    } catch (e: Exception) {
                        screenModelScope.launch {
                            handleError(e)
                        }
                        null
                    }
                }?.let { withdrawalsList ->
                    withdrawals.clear()
                    withdrawals.addAll(withdrawalsList)
                    withdrawalsNotification = withdrawals.count { !it.isSeen }
                    withdrawals.filter { !it.isSeen }.forEach { _ ->
                        SystemNotificationService.notify(
                            title = "Withdrawal Request",
                            message = "New response received for withdrawal request",
                        )
                    }
                }
            }
    }

    override fun onDestroy() {
        if (::listener.isInitialized) {
            listener.remove()
        }
        super.onDestroy()
    }

}
