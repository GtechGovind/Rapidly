package com.gtech.rapidly.features.domain.admin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.google.firebase.firestore.ListenerRegistration
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.model.Withdraw
import com.gtech.rapidly.features.common.firestore.service.UserService
import com.gtech.rapidly.features.common.firestore.service.WithdrawalService
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import com.gtech.rapidly.features.common.notification.SystemNotificationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AdminMainViewModel : ScreenModel() {

    var title by mutableStateOf("Admin Dashboard")
    var withdraws = mutableStateListOf<Pair<Withdraw, User>>()
    var withdrawNotification by mutableIntStateOf(0)

    private var withdrawListener: ListenerRegistration? = null

    companion object {
        var instance: AdminMainViewModel? = null
    }

    override suspend fun onCreated() {
        super.onCreated()
        instance = this
        startListeners()
    }

    private fun startListeners() {
        // WITHDRAW LISTENER
        screenModelScope.launch(Dispatchers.IO) {
            withdrawListener = WithdrawalService.getWithdrawals()
                ?.addSnapshotListener { value, error ->
                    if (error != null) {
                        runBlocking { handleError(error) }
                        return@addSnapshotListener
                    }
                    withdraws.clear()
                    withdrawNotification = 0
                    value?.documents?.forEach { doc ->
                        val withdraw = doc.toObject(Withdraw::class.java)
                        ?: return@forEach
                        val user = runBlocking { UserService.getUserByPhoneNumber(withdraw.requestedBy.toString()) }
                        ?: return@forEach
                        withdraws.add(Pair(withdraw, user))
                    }
                    withdraws.forEach {
                        if (it.first.status == Withdraw.Status.PENDING) {
                            withdrawNotification++
                            SystemNotificationService.notify(
                                title = "Withdrawal Request 🚀",
                                message = "A new withdrawal request has been made by ${it.first.requestedBy}",
                            )
                        }
                    }
                }
        }

    }

    override fun onDestroy() {
        withdrawListener?.remove()
        super.onDestroy()
    }

}