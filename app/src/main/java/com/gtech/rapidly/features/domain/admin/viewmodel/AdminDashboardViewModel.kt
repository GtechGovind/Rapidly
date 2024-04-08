package com.gtech.rapidly.features.domain.admin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.gtech.rapidly.features.common.firestore.model.Accounting
import com.gtech.rapidly.features.common.firestore.model.User
import com.gtech.rapidly.features.common.firestore.service.AccountingService
import com.gtech.rapidly.features.common.firestore.service.UserService
import com.gtech.rapidly.features.common.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AdminDashboardViewModel : ViewModel() {

    var reports by mutableStateOf<Accounting?>(null)
    var users = mutableStateListOf<User>()
    override suspend fun onCreated() {

        // Set reactive data for reports
        viewModelScope.launch(Dispatchers.IO) {
            AccountingService
                .getReportDf()
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        runBlocking { handleError(Exception(e)) }
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {
                        reports = snapshot.toObject(Accounting::class.java)
                    }
                }
        }

        // Get all users
        viewModelScope.launch(Dispatchers.IO) {
            UserService.getUsersDf()
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        runBlocking { handleError(Exception(e)) }
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        users.clear()
                        snapshot.documents.forEach {
                            users.add(it.toObject(User::class.java)!!)
                        }
                    }
                }
        }

    }

    fun toggleUserStatus(
        user: User
    ) = viewModelScope.launch(Dispatchers.IO) {
        withLoading {
            if (user.status == User.Status.ACTIVE) user.status = User.Status.INACTIVE
            else user.status = User.Status.ACTIVE
            if (!UserService.saveOrUpdate(user)) handleError(Exception("Failed to update user status"))
            else showMessage("${user.name} status updated successfully")
        }
    }

}