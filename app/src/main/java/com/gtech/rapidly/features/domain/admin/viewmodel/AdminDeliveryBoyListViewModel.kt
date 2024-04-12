package com.gtech.rapidly.features.domain.admin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.gtech.rapidly.features.common.firestore.model.Penalty
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.service.AccountingService
import com.gtech.rapidly.features.common.firestore.service.PenaltyService
import com.gtech.rapidly.features.common.firestore.service.UserService
import com.gtech.rapidly.features.common.lifecycle.ScreenModel
import com.gtech.rapidly.utils.misc.RuntimeCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AdminDeliveryBoyListViewModel : ScreenModel() {

    private var _users = mutableListOf<User>()
    private var userListener: ListenerRegistration? = null
    private var penaltyUser by mutableStateOf<User?>(null)

    var users: List<User> by mutableStateOf(emptyList())
    var query by mutableStateOf(TextFieldValue())

    var showPenaltyDialog by mutableStateOf(false)
    var penaltyAmount by mutableStateOf(TextFieldValue())
    var penaltyReason by mutableStateOf(TextFieldValue())

    override suspend fun onCreated() {
        super.onCreated()
        startListeners()
    }

    private fun startListeners() {
        screenModelScope.launch(Dispatchers.IO) {
            userListener = UserService.getUsersDf()
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        runBlocking { handleError(error) }
                        return@addSnapshotListener
                    }
                    _users.clear()
                    value?.documents?.forEach { doc ->
                        if (doc.exists()) {
                            doc.toObject(User::class.java)?.let {
                                _users.add(it)
                            }
                        }
                    }
                    users = _users
                }
        }
    }

    fun filterUsers() = screenModelScope.launch(
        Dispatchers.Default
    ) {
        val filter = query.text
        users = if (filter.isEmpty()) {
            _users
        } else {
            _users.filter {
                it.name.contains(filter, ignoreCase = true)
            }
        }
    }

    fun toggleUserStatus(
        user: User
    ) = screenModelScope.launch(Dispatchers.IO) {
        withLoading {
            if (user.status == User.Status.ACTIVE) user.status = User.Status.INACTIVE
            else user.status = User.Status.ACTIVE
            if (!UserService.saveOrUpdate(user)) {
                handleError(Exception("Failed to update user status"))
            } else {
                showMessage(
                    if (user.status == User.Status.ACTIVE) {
                        "User ${user.name} is now active"
                    } else {
                        "User ${user.name} is now inactive"
                    }
                )
            }
        }
    }

    fun showPenaltyDialog(user: User) {
        penaltyUser = user
        showPenaltyDialog = true
    }

    fun applyPenalty() = screenModelScope.launch(
        Dispatchers.IO
    ) {

        withLoading {

            val user = penaltyUser ?: run {
                showMessage("User not found")
                return@withLoading
            }

            val amount = penaltyAmount.text.toDoubleOrNull() ?: run {
                showMessage("Invalid amount")
                return@withLoading
            }

            val reason = penaltyReason.text
            if (reason.isEmpty()) {
                showMessage("Reason cannot be empty")
                return@withLoading
            }

            val penalty = Penalty(
                amount = amount,
                reason = reason,
                penalisedTo = user.phoneNumber,
                penalisedBy = RuntimeCache.getCurrentUser().phoneNumber
            )

            user.totalPenalties += amount

            val accounting = AccountingService.getReport()
            if (accounting == null) {
                showMessage("Failed to get accounting report")
                return@withLoading
            }

            accounting.totalDeliveryBoyPenalty += amount

            FirebaseFirestore.getInstance().runTransaction {
                runBlocking {
                    if (!UserService.saveOrUpdate(user)) throw Exception("Failed to update user")
                    if (!PenaltyService.saveOrUpdate(penalty)) throw Exception("Failed to penalty")
                    if(!AccountingService.saveOrUpdate(accounting)) throw Exception("Failed to update accounting")
                }
            }

            showMessage("Penalty applied successfully")
            showPenaltyDialog = false
            penaltyUser = null

        }

    }

    override fun onDestroy() {
        userListener?.remove()
        super.onDestroy()
    }
}